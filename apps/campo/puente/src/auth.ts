// Autenticación de transportistas y técnicos sin cuenta de Odoo.
// La persona es una ficha de empleado (hr.employee) con campos x_app_*.
// La contraseña se guarda solo como hash PBKDF2-SHA256; el puente emite un token HMAC de 12 h.

import { Odoo } from "./odoo";

export type Rol = "transportista" | "tecnico";

export interface Operador {
  id: number; // id de hr.employee
  nombre: string;
  rol: Rol;
  empresa: string | null;
}

export class AuthError extends Error {
  constructor(message: string, public readonly codigo = "NO_AUTORIZADO") {
    super(message);
  }
}

const enc = new TextEncoder();

function b64url(bytes: ArrayBuffer | Uint8Array): string {
  const arr = bytes instanceof Uint8Array ? bytes : new Uint8Array(bytes);
  let s = "";
  for (const b of arr) s += String.fromCharCode(b);
  return btoa(s).replace(/\+/g, "-").replace(/\//g, "_").replace(/=+$/, "");
}

function deB64url(s: string): Uint8Array {
  const pad = s.replace(/-/g, "+").replace(/_/g, "/") + "===".slice((s.length + 3) % 4);
  const bin = atob(pad);
  const out = new Uint8Array(bin.length);
  for (let i = 0; i < bin.length; i++) out[i] = bin.charCodeAt(i);
  return out;
}

function igualesTiempoConstante(a: Uint8Array, b: Uint8Array): boolean {
  if (a.length !== b.length) return false;
  let r = 0;
  for (let i = 0; i < a.length; i++) r |= a[i] ^ b[i];
  return r === 0;
}

async function pbkdf2(password: string, sal: Uint8Array, iter: number): Promise<Uint8Array> {
  const key = await crypto.subtle.importKey("raw", enc.encode(password), "PBKDF2", false, ["deriveBits"]);
  const bits = await crypto.subtle.deriveBits({ name: "PBKDF2", hash: "SHA-256", salt: sal, iterations: iter }, key, 256);
  return new Uint8Array(bits);
}

/** Formato: pbkdf2_sha256$<iteraciones>$<sal>$<hash> (base64url). */
export async function hashPassword(password: string, iter = 20000): Promise<string> {
  const sal = crypto.getRandomValues(new Uint8Array(16));
  const h = await pbkdf2(password, sal, iter);
  return `pbkdf2_sha256$${iter}$${b64url(sal)}$${b64url(h)}`;
}

export async function verificarPassword(password: string, guardado: string): Promise<boolean> {
  const partes = (guardado || "").split("$");
  if (partes.length !== 4 || partes[0] !== "pbkdf2_sha256") return false;
  const iter = parseInt(partes[1], 10);
  const h = await pbkdf2(password, deB64url(partes[2]), iter);
  return igualesTiempoConstante(h, deB64url(partes[3]));
}

export function validarPasswordNueva(p: string): string | null {
  if (!p || p.length < 8) return "La contraseña debe tener al menos 8 caracteres.";
  if (!/[0-9]/.test(p) || !/[A-Za-zÁÉÍÓÚáéíóúÑñ]/.test(p)) return "Usa letras y números.";
  return null;
}

// ---------- Tokens ----------

async function hmac(secret: string, data: string): Promise<Uint8Array> {
  const key = await crypto.subtle.importKey("raw", enc.encode(secret), { name: "HMAC", hash: "SHA-256" }, false, ["sign"]);
  return new Uint8Array(await crypto.subtle.sign("HMAC", key, enc.encode(data)));
}

export interface TokenData {
  sub: number;
  rol: Rol;
  nom: string;
  exp: number; // segundos epoch
  cp?: boolean; // debe cambiar contraseña
}

export async function firmarToken(secret: string, data: TokenData): Promise<string> {
  const cuerpo = b64url(enc.encode(JSON.stringify(data)));
  return `${cuerpo}.${b64url(await hmac(secret, cuerpo))}`;
}

export async function leerToken(secret: string, token: string, ahora = Date.now()): Promise<TokenData> {
  const [cuerpo, firma] = (token || "").split(".");
  if (!cuerpo || !firma) throw new AuthError("Sesión inválida");
  const esperada = await hmac(secret, cuerpo);
  if (!igualesTiempoConstante(esperada, deB64url(firma))) throw new AuthError("Sesión inválida");
  const data = JSON.parse(new TextDecoder().decode(deB64url(cuerpo))) as TokenData;
  if (data.exp * 1000 < ahora) throw new AuthError("La sesión expiró, vuelve a entrar", "SESION_EXPIRADA");
  return data;
}

// ---------- Login contra Odoo ----------

const CAMPOS_EMP = [
  "id", "name", "x_app_rol", "x_app_activo", "x_app_password_hash", "x_app_password_temporal", "company_id",
  "x_app_intentos", "x_app_bloqueado_hasta",
];
const MAX_INTENTOS = 5;
const MINUTOS_BLOQUEO = 15;

export interface ResultadoLogin {
  token: string;
  operador: Operador;
  debe_cambiar_password: boolean;
}

export async function login(
  odoo: Odoo,
  secret: string,
  usuario: string,
  password: string,
  horas = 12,
): Promise<ResultadoLogin> {
  const u = (usuario || "").trim().toLowerCase();
  if (!u || !password) throw new AuthError("Escribe usuario y contraseña", "DATOS_INCOMPLETOS");
  const emps = await odoo.searchRead("hr.employee", [["x_app_usuario", "=ilike", u], ["x_app_activo", "=", true]], CAMPOS_EMP, {
    limit: 2,
  });
  if (emps.length !== 1) throw new AuthError("Usuario o contraseña incorrectos", "CREDENCIALES");
  const e = emps[0];
  if (e.x_app_bloqueado_hasta && Date.parse(e.x_app_bloqueado_hasta.replace(" ", "T") + "Z") > Date.now()) {
    throw new AuthError(`Demasiados intentos. Espera ${MINUTOS_BLOQUEO} minutos o pide a la oficina que te desbloquee.`, "BLOQUEADO");
  }
  let debeCambiar = false;
  let valido = false;
  if (e.x_app_password_hash) {
    valido = await verificarPassword(password, e.x_app_password_hash);
  } else if (e.x_app_password_temporal && e.x_app_password_temporal === password) {
    valido = true;
    debeCambiar = true; // primera vez: contraseña temporal que puso la oficina
  }
  if (!valido) {
    const intentos = (e.x_app_intentos || 0) + 1;
    const vals: Record<string, unknown> = { x_app_intentos: intentos };
    if (intentos >= MAX_INTENTOS) {
      vals.x_app_bloqueado_hasta = new Date(Date.now() + MINUTOS_BLOQUEO * 60000).toISOString().slice(0, 19).replace("T", " ");
      vals.x_app_intentos = 0;
    }
    await odoo.write("hr.employee", [e.id], vals);
    throw new AuthError("Usuario o contraseña incorrectos", "CREDENCIALES");
  }
  if (e.x_app_intentos) await odoo.write("hr.employee", [e.id], { x_app_intentos: 0 });
  if (e.x_app_rol !== "transportista" && e.x_app_rol !== "tecnico") throw new AuthError("Tu ficha no tiene un rol de la app asignado", "SIN_ROL");
  const operador: Operador = { id: e.id, nombre: e.name.trim(), rol: e.x_app_rol, empresa: e.company_id ? e.company_id[1] : null };
  const token = await firmarToken(secret, {
    sub: e.id, rol: operador.rol, nom: operador.nombre, exp: Math.floor(Date.now() / 1000) + horas * 3600, cp: debeCambiar || undefined,
  });
  return { token, operador, debe_cambiar_password: debeCambiar };
}

export async function cambiarPassword(odoo: Odoo, empId: number, nueva: string, iter?: number): Promise<void> {
  const error = validarPasswordNueva(nueva);
  if (error) throw new AuthError(error, "PASSWORD_DEBIL");
  const hash = await hashPassword(nueva, iter);
  await odoo.write("hr.employee", [empId], { x_app_password_hash: hash, x_app_password_temporal: false });
}

/** Confirma que la ficha sigue activa y con el mismo rol (la oficina puede dar de baja en cualquier momento). */
export async function verificarActivo(odoo: Odoo, t: TokenData): Promise<void> {
  const [e] = await odoo.read("hr.employee", [t.sub], ["x_app_activo", "x_app_rol"]);
  if (!e || !e.x_app_activo || e.x_app_rol !== t.rol) throw new AuthError("Tu acceso a la app fue desactivado", "DESACTIVADO");
}
