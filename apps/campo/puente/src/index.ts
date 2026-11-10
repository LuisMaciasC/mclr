// Servidor puente de la App de campo (Cloudflare Workers).
// Rutas:
//   GET  /v1/salud              -> estado del puente (sin datos)
//   POST /v1/sesion             -> {usuario, password} => token
//   POST /v1/sesion/password    -> {password_nueva}    (con token)
//   GET  /v1/tareas             -> tareas asignadas a la persona del token
//   POST /v1/eventos            -> aplica un evento (idempotente por uuid)

import { Odoo, OdooEnv, OdooError } from "./odoo";
import { AuthError, cambiarPassword, leerToken, login, Operador, TokenData, verificarActivo } from "./auth";
import { tareasDe } from "./tareas";
import { EventoError, procesarEvento } from "./eventos";

export const VERSION = "1.0.0";

interface Limitador {
  limit(o: { key: string }): Promise<{ success: boolean }>;
}

export interface Env extends OdooEnv {
  TOKEN_SECRET: string; // secreto para firmar sesiones (32+ caracteres aleatorios)
  LIMITE_SESION?: Limitador; // limitador gratuito de Cloudflare: intentos de inicio de sesión por IP
  PBKDF2_ITER?: string;
  SESION_HORAS?: string;
}

type FetchLike = (input: string, init?: RequestInit) => Promise<Response>;

function json(data: unknown, status = 200): Response {
  return new Response(JSON.stringify(data), {
    status,
    headers: { "Content-Type": "application/json; charset=utf-8", "Cache-Control": "no-store" },
  });
}

function error(status: number, codigo: string, mensaje: string, reintentar = false): Response {
  return json({ ok: false, codigo, mensaje, reintentar }, status);
}

async function cuerpo(req: Request): Promise<any> {
  try {
    return await req.json();
  } catch {
    throw new EventoError("El cuerpo no es JSON válido.", "JSON", 400);
  }
}

async function sesion(req: Request, env: Env, odoo: Odoo, permitirCambio = false): Promise<{ t: TokenData; op: Operador }> {
  const h = req.headers.get("Authorization") || "";
  if (!h.startsWith("Bearer ")) throw new AuthError("Falta iniciar sesión");
  const t = await leerToken(env.TOKEN_SECRET, h.slice(7));
  if (t.cp && !permitirCambio) throw new AuthError("Debes cambiar tu contraseña antes de continuar", "CAMBIAR_PASSWORD");
  await verificarActivo(odoo, t);
  return { t, op: { id: t.sub, nombre: t.nom, rol: t.rol, empresa: null } };
}

export async function manejar(req: Request, env: Env, fetchImpl: FetchLike = fetch): Promise<Response> {
  const url = new URL(req.url);
  const ruta = `${req.method} ${url.pathname.replace(/\/+$/, "")}`;
  const faltan = ["ODOO_URL", "ODOO_DB", "ODOO_LOGIN", "ODOO_API_KEY", "TOKEN_SECRET"].filter((k) => !(env as any)[k]);
  if (faltan.length) return error(500, "CONFIGURACION", `Faltan variables del puente: ${faltan.join(", ")}`);
  const odoo = new Odoo(env, fetchImpl);

  // Freno a ataques de fuerza bruta: pocas peticiones de sesión por minuto desde una misma IP.
  if (env.LIMITE_SESION && req.method === "POST" && url.pathname.startsWith("/v1/sesion")) {
    const ip = req.headers.get("CF-Connecting-IP") || "sin-ip";
    const { success } = await env.LIMITE_SESION.limit({ key: ip });
    if (!success) return error(429, "DEMASIADOS_INTENTOS", "Demasiados intentos desde esta conexión. Espera un minuto.", true);
  }

  try {
    switch (ruta) {
      case "GET /v1/salud":
        return json({ ok: true, version: VERSION });

      case "POST /v1/sesion": {
        const b = await cuerpo(req);
        const r = await login(odoo, env.TOKEN_SECRET, b.usuario, b.password, Number(env.SESION_HORAS || 12));
        return json({ ok: true, ...r });
      }

      case "POST /v1/sesion/password": {
        const { t } = await sesion(req, env, odoo, true);
        const b = await cuerpo(req);
        await cambiarPassword(odoo, t.sub, b.password_nueva, Number(env.PBKDF2_ITER || 20000));
        // Nueva sesión sin la marca de "debe cambiar", con el usuario de la sesión (nunca el que mande el teléfono).
        const [emp] = await odoo.read("hr.employee", [t.sub], ["x_app_usuario"]);
        const r = await login(odoo, env.TOKEN_SECRET, emp.x_app_usuario, b.password_nueva, Number(env.SESION_HORAS || 12));
        return json({ ok: true, ...r });
      }

      case "GET /v1/tareas": {
        const { op } = await sesion(req, env, odoo);
        const tareas = await tareasDe(odoo, op);
        return json({ ok: true, servidor: new Date().toISOString(), tareas });
      }

      case "POST /v1/eventos": {
        const { op } = await sesion(req, env, odoo);
        const ev = await cuerpo(req);
        const r = await procesarEvento(odoo, op, ev);
        return json(r, r.ok ? 200 : 409);
      }

      default:
        return error(404, "NO_ENCONTRADO", "Ruta no encontrada");
    }
  } catch (e) {
    if (e instanceof AuthError) return error(401, e.codigo, e.message);
    if (e instanceof EventoError) return error(e.http, e.codigo, e.message, e.reintentar);
    if (e instanceof OdooError) {
      const deNegocio = /ValidationError|UserError|AccessError|MissingError/.test(String(e.detalle || ""));
      return deNegocio
        ? error(422, "ODOO_RECHAZO", `Odoo rechazó la operación: ${e.message}`, false)
        : error(502, "ODOO", `No se pudo hablar con Odoo: ${e.message}`, true);
    }
    console.error(e);
    return error(500, "INTERNO", "Error interno del puente", true);
  }
}

export default {
  fetch(req: Request, env: Env): Promise<Response> {
    return manejar(req, env);
  },
};
