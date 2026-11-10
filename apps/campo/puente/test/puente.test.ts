import { beforeEach, describe, expect, it } from "vitest";
import { manejar, Env } from "../src/index";
import { hashPassword } from "../src/auth";
import { IMG, OdooFalso } from "./odoo-falso";

const env: Env = {
  ODOO_URL: "https://odoo.prueba",
  ODOO_DB: "prueba",
  ODOO_LOGIN: "integracion",
  ODOO_API_KEY: "llave-secreta",
  TOKEN_SECRET: "secreto-de-pruebas-muy-largo-1234567890",
  PBKDF2_ITER: "1000",
};

let o: OdooFalso;

async function llamar(metodo: string, ruta: string, body?: unknown, token?: string) {
  const req = new Request(`https://puente.prueba${ruta}`, {
    method: metodo,
    headers: { "Content-Type": "application/json", ...(token ? { Authorization: `Bearer ${token}` } : {}) },
    body: body ? JSON.stringify(body) : undefined,
  });
  const r = await manejar(req, env, o.fetch);
  return { status: r.status, body: (await r.json()) as any };
}

async function entrar(usuario: string, password: string) {
  const r = await llamar("POST", "/v1/sesion", { usuario, password });
  expect(r.status).toBe(200);
  return r.body.token as string;
}

function uuid() {
  return crypto.randomUUID();
}

beforeEach(async () => {
  o = new OdooFalso();
  o.agregar("res.currency", { id: 33, name: "MXN" });
  o.agregar("res.partner", { id: 300, name: "Cliente Prueba", street: "Av. Reforma 10", street2: "Portón negro", l10n_mx_edi_colony: "Centro", zip: "06000", city: "CDMX", state_id: [9, "Ciudad de México (MX)"], mobile: "5512345678" });
  o.agregar("res.partner", { id: 301, name: "PRUEBAS IT", street: "Calle Origen 1", zip: "01000", city: "CDMX" });
  o.agregar("res.company", { id: 186, name: "PRUEBAS IT", partner_id: [301, "PRUEBAS IT"] });
  o.agregar("sale.order", { id: 29761, name: "S29761", partner_id: [300, "Cliente Prueba"], commitment_date: "2026-11-11 18:00:00", x_studio_estado_de_la_entrega: "pending" });
  o.agregar("hr.employee", { id: 501, name: "Chofer Prueba", x_app_usuario: "chofer1", x_app_activo: true, x_app_rol: "transportista", x_app_password_temporal: "Temporal1", company_id: [186, "PRUEBAS IT"] });
  o.agregar("hr.employee", { id: 502, name: "Técnico Prueba", x_app_usuario: "tecnico1", x_app_activo: true, x_app_rol: "tecnico", x_app_password_hash: await hashPassword("Segura123", 1000), company_id: [186, "PRUEBAS IT"] });
  o.agregar("hr.employee", { id: 503, name: "Otro Chofer", x_app_usuario: "chofer2", x_app_activo: true, x_app_rol: "transportista", x_app_password_hash: await hashPassword("Segura123", 1000), company_id: [186, "PRUEBAS IT"] });
  o.agregar("stock.move", { id: 1, picking_id: [9001, "prueb/OUT/00009"], product_id: [10, "Silla ejecutiva"], product_uom_qty: 4, product_uom: [1, "Unidades"] });
  const base = { picking_type_code: "outgoing", state: "assigned", company_id: [186, "PRUEBAS IT"], partner_id: [300, "Cliente Prueba"], sale_id: [29761, "S29761"], priority: "0", scheduled_date: "2026-11-10 15:00:00", date_deadline: "2026-11-11 18:00:00" };
  o.agregar("stock.picking", { ...base, id: 9001, name: "prueb/OUT/00009", move_ids: [1], x_app_transportista_id: [501, "Chofer Prueba"], x_app_modalidad: "propio", x_app_estado: "asignado", note: "<p>Llamar antes de llegar</p>" });
  o.agregar("stock.picking", { ...base, id: 9002, name: "prueb/OUT/00010", move_ids: [], x_app_transportista_id: [501, "Chofer Prueba"], x_app_modalidad: "paqueteria", x_app_estado: "asignado", sale_id: false });
  o.agregar("stock.picking", { ...base, id: 9003, name: "prueb/OUT/00011", move_ids: [], x_app_transportista_id: [503, "Otro Chofer"], x_app_modalidad: "propio", x_app_estado: "asignado" });
  for (const [id, name] of [[61, "Asignado"], [62, "En camino"], [63, "En servicio"], [64, "Pendiente"], [65, "Finalizado"], [66, "Cancelado"]] as const) {
    o.agregar("project.task.type", { id, name, project_ids: [40] });
  }
  o.agregar("project.task", { id: 8001, name: "Instalación de rockola", project_id: [40, "Servicios técnicos"], stage_id: [61, "Asignado"], x_app_tecnico_id: [502, "Técnico Prueba"], partner_id: [300, "Cliente Prueba"], x_app_tipo_servicio: "instalacion", date_deadline: "2026-11-12 20:00:00", priority: "1", company_id: [186, "PRUEBAS IT"] });
});

describe("sesión", () => {
  it("rechaza contraseña equivocada y bloquea tras 5 intentos", async () => {
    for (let i = 0; i < 5; i++) {
      const r = await llamar("POST", "/v1/sesion", { usuario: "tecnico1", password: "mala" });
      expect(r.status).toBe(401);
    }
    const r = await llamar("POST", "/v1/sesion", { usuario: "tecnico1", password: "Segura123" });
    expect(r.body.codigo).toBe("BLOQUEADO");
  });

  it("primera entrada con temporal obliga a cambiar contraseña y guarda solo el hash", async () => {
    const r = await llamar("POST", "/v1/sesion", { usuario: "CHOFER1", password: "Temporal1" });
    expect(r.body.debe_cambiar_password).toBe(true);
    const bloqueado = await llamar("GET", "/v1/tareas", undefined, r.body.token);
    expect(bloqueado.body.codigo).toBe("CAMBIAR_PASSWORD");
    const debil = await llamar("POST", "/v1/sesion/password", { usuario: "chofer1", password_nueva: "corta" }, r.body.token);
    expect(debil.status).toBe(401);
    const ok = await llamar("POST", "/v1/sesion/password", { usuario: "chofer1", password_nueva: "NuevaClave9" }, r.body.token);
    expect(ok.status).toBe(200);
    const emp = o.get("hr.employee", 501)!;
    expect(emp.x_app_password_hash).toMatch(/^pbkdf2_sha256\$/);
    expect(emp.x_app_password_temporal).toBe(false);
    expect(JSON.stringify(o.db["hr.employee"])).not.toContain("NuevaClave9");
  });

  it("los comodines % y _ en el usuario no permiten entrar con otra cuenta", async () => {
    for (const u of ["%", "chofer%", "chofer_", "_ecnico1"]) {
      const r = await llamar("POST", "/v1/sesion", { usuario: u, password: "Segura123" });
      expect(r.status).toBe(401);
    }
    expect(o.get("hr.employee", 503)!.x_app_intentos || 0).toBe(0);
  });

  it("un token alterado no sirve", async () => {
    const t = await entrar("tecnico1", "Segura123");
    const r = await llamar("GET", "/v1/tareas", undefined, t.slice(0, -2) + "xx");
    expect(r.status).toBe(401);
  });

  it("si la oficina desactiva la ficha, el token deja de servir", async () => {
    const t = await entrar("tecnico1", "Segura123");
    o.get("hr.employee", 502)!.x_app_activo = false;
    const r = await llamar("GET", "/v1/tareas", undefined, t);
    expect(r.body.codigo).toBe("DESACTIVADO");
  });
});

describe("entregas", () => {
  let token: string;
  beforeEach(async () => {
    o.get("hr.employee", 501)!.x_app_password_hash = await hashPassword("Segura123", 1000);
    token = await entrar("chofer1", "Segura123");
  });

  it("solo lista las entregas asignadas a la persona, con datos completos", async () => {
    const r = await llamar("GET", "/v1/tareas", undefined, token);
    expect(r.status).toBe(200);
    const ids = r.body.tareas.map((t: any) => t.id).sort();
    expect(ids).toEqual([9001, 9002]);
    const t = r.body.tareas.find((x: any) => x.id === 9001);
    expect(t.pedido).toBe("S29761");
    expect(t.productos[0]).toMatchObject({ producto: "Silla ejecutiva", cantidad: 4 });
    expect(t.direccion.texto).toContain("C.P. 06000");
    expect(t.direccion.referencias).toBe("Portón negro");
    expect(t.instrucciones).toBe("Llamar antes de llegar");
    expect(t.sla.limite).toBe("2026-11-11T18:00:00Z");
    expect(t.remitente.nombre).toBe("PRUEBAS IT");
  });

  it("flujo directo: iniciar, confirmar con foto y firma; venta queda Entregado", async () => {
    const r1 = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "iniciar_traslado", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T16:00:00Z" }, token);
    expect(r1.body).toMatchObject({ ok: true, estado: "en_traslado" });
    const sinFirma = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "entregado_cliente", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T17:00:00Z", datos: { recibe_nombre: "Ana" }, adjuntos: [{ rol: "foto", base64: IMG }] }, token);
    expect(sinFirma.status).toBe(400);
    expect(sinFirma.body.codigo).toBe("FALTA_FIRMA");
    const r2 = await llamar("POST", "/v1/eventos", {
      uuid: uuid(), tipo: "entregado_cliente", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T17:00:00Z", lat: 19.43, lon: -99.13,
      datos: { recibe_nombre: "Ana López" }, adjuntos: [{ rol: "foto", base64: IMG }, { rol: "firma", base64: IMG, mimetype: "image/png" }],
    }, token);
    expect(r2.body).toMatchObject({ ok: true, estado: "entregado_cliente" });
    const p = o.get("stock.picking", 9001)!;
    expect(p.x_app_estado).toBe("entregado_cliente");
    expect(p.signature).toBe(IMG);
    expect(p.x_app_recibe_nombre).toBe("Ana López");
    expect(p.state).toBe("assigned"); // no se valida inventario
    expect(o.get("sale.order", 29761)!.x_studio_estado_de_la_entrega).toBe("partial"); // la 9003 sigue pendiente
    expect(o.tabla("ir.attachment").filter((a) => a.res_id === 9001)).toHaveLength(2);
    const ev = o.tabla("x_app_evento").filter((e) => e.x_res_id === 9001);
    expect(ev.map((e) => e.x_resultado)).toEqual(["aplicado", "aplicado"]);
    expect(ev[1].x_operador_id[0]).toBe(501);
    expect(ev[1].x_fecha_dispositivo).toBe("2026-11-10 17:00:00");
    expect(o.mensajes.at(-1)!.body).toContain("Chofer Prueba");
    expect(o.mensajes.at(-1)!.attachment_ids).toHaveLength(2);
  });

  it("reintentar el mismo folio no duplica nada", async () => {
    const ev = { uuid: uuid(), tipo: "entregado_cliente", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T17:00:00Z", datos: { recibe_nombre: "Ana" }, adjuntos: [{ rol: "foto", base64: IMG }, { rol: "firma", base64: IMG }] };
    const a = await llamar("POST", "/v1/eventos", ev, token);
    const b = await llamar("POST", "/v1/eventos", ev, token);
    expect(a.body.ok).toBe(true);
    expect(b.body).toMatchObject({ ok: true, repetido: true, evento_id: a.body.evento_id });
    expect(o.tabla("x_app_evento")).toHaveLength(1);
    expect(o.tabla("ir.attachment")).toHaveLength(2);
    expect(o.mensajes).toHaveLength(1);
  });

  it("si se cae la red a la mitad, el reintento termina sin duplicar adjuntos", async () => {
    const ev = { uuid: uuid(), tipo: "entregado_cliente", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T17:00:00Z", datos: { recibe_nombre: "Ana" }, adjuntos: [{ rol: "foto", base64: IMG }, { rol: "firma", base64: IMG }] };
    o.fallarSiguiente = "message_post";
    const a = await llamar("POST", "/v1/eventos", ev, token);
    expect(a.status).toBe(502);
    expect(a.body.reintentar).toBe(true);
    const b = await llamar("POST", "/v1/eventos", ev, token);
    expect(b.body.ok).toBe(true);
    expect(o.tabla("x_app_evento")).toHaveLength(1);
    expect(o.tabla("ir.attachment")).toHaveLength(2);
  });

  it("entrega a paquetería registra guía, costo y comprobante; no marca la venta", async () => {
    const r = await llamar("POST", "/v1/eventos", {
      uuid: uuid(), tipo: "entregado_paqueteria", tarea: { tipo: "entrega", id: 9002 }, fecha_dispositivo: "2026-11-10T18:00:00Z",
      datos: { paqueteria: "Estafeta", guia: "123456789", precio: 350.5, moneda: "MXN", fecha_entrega: "2026-11-10T17:55:00Z" },
      adjuntos: [{ rol: "comprobante", base64: IMG }],
    }, token);
    expect(r.body).toMatchObject({ ok: true, estado: "entregado_paqueteria" });
    const p = o.get("stock.picking", 9002)!;
    expect(p).toMatchObject({ x_app_paqueteria: "Estafeta", x_app_guia: "123456789", x_app_costo_envio: 350.5, x_app_factura_paqueteria: false });
    expect(p.x_app_moneda_id[0]).toBe(33);
  });

  it("no entregada: guarda motivo y no la marca como entregada", async () => {
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "no_entregado", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T18:00:00Z", datos: { motivo: "Cliente ausente", observaciones: "Nadie abrió" } }, token);
    expect(r.body.estado).toBe("no_entregado");
    expect(o.get("stock.picking", 9001)!.x_app_motivo_no_entrega).toBe("Cliente ausente — Nadie abrió");
    expect(o.get("sale.order", 29761)!.x_studio_estado_de_la_entrega).toBe("pending");
  });

  it("entrega reasignada a otra persona: se rechaza pero las evidencias quedan en Odoo", async () => {
    // Historial de Odoo: la entrega 9003 era de chofer1 (501) y la oficina la pasó a 503.
    o.agregar("mail.message", { id: 1, model: "stock.picking", res_id: 9003 });
    o.agregar("ir.model.fields", { id: 1, name: "x_app_transportista_id" });
    o.agregar("mail.tracking.value", { id: 1, mail_message_id: [1, "msg"], field_id: [1, "campo"], old_value_integer: 501, new_value_integer: 503 });
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "no_entregado", tarea: { tipo: "entrega", id: 9003 }, fecha_dispositivo: "2026-11-10T18:00:00Z", datos: { motivo: "x" }, adjuntos: [{ rol: "foto", base64: IMG }] }, token);
    expect(r.status).toBe(409);
    expect(r.body).toMatchObject({ resultado: "rechazado", codigo: "REASIGNADA" });
    expect(o.get("stock.picking", 9003)!.x_app_estado).toBe("asignado");
    expect(o.tabla("ir.attachment")).toHaveLength(1);
    expect(o.tabla("x_app_evento")[0].x_resultado).toBe("rechazado");
  });

  it("tarea que nunca fue suya: no se guarda nada, ni fotos ni bitácora", async () => {
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "no_entregado", tarea: { tipo: "entrega", id: 9003 }, fecha_dispositivo: "2026-11-10T18:00:00Z", datos: { motivo: "x" }, adjuntos: [{ rol: "foto", base64: IMG }] }, token);
    expect(r.status).toBe(403);
    expect(r.body.codigo).toBe("SIN_ACCESO");
    expect(o.tabla("ir.attachment")).toHaveLength(0);
    expect(o.tabla("x_app_evento")).toHaveLength(0);
    expect(o.mensajes).toHaveLength(0);
  });

  it("solo acepta fotos JPEG o PNG", async () => {
    const html = btoa("<html><script>alert(1)</script></html>".repeat(10));
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "no_entregado", tarea: { tipo: "entrega", id: 9001 }, fecha_dispositivo: "2026-11-10T18:00:00Z", datos: { motivo: "x" }, adjuntos: [{ rol: "foto", base64: html }] }, token);
    expect(r.status).toBe(400);
    expect(r.body.codigo).toBe("ARCHIVO_INVALIDO");
    expect(o.tabla("ir.attachment")).toHaveLength(0);
  });

  it("un transportista no puede mover servicios", async () => {
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "aceptar", tarea: { tipo: "servicio", id: 8001 }, fecha_dispositivo: "2026-11-10T18:00:00Z" }, token);
    expect(r.status).toBe(400);
  });
});

describe("servicios", () => {
  let token: string;
  beforeEach(async () => {
    token = await entrar("tecnico1", "Segura123");
  });

  it("flujo completo: aceptar, materiales, en camino, en servicio, pendiente, finalizar", async () => {
    const lista = await llamar("GET", "/v1/tareas", undefined, token);
    expect(lista.body.tareas).toHaveLength(1);
    expect(lista.body.tareas[0]).toMatchObject({ tipo: "servicio", prioridad: "urgente", estado: "asignado" });

    const e = (tipo: string, extra: any = {}) =>
      llamar("POST", "/v1/eventos", { uuid: uuid(), tipo, tarea: { tipo: "servicio", id: 8001 }, fecha_dispositivo: new Date().toISOString(), ...extra }, token);

    expect((await e("aceptar")).body.ok).toBe(true);
    expect(o.get("project.task", 8001)!.x_app_aceptado_el).toBeTruthy();
    expect((await e("materiales_solicitados", { datos: { materiales: [{ descripcion: "Cable HDMI 3 m", cantidad: 2, observaciones: "Negro" }] } })).body.ok).toBe(true);
    expect((await e("en_camino")).body.estado).toBe("En camino");
    expect((await e("en_servicio")).body.estado).toBe("En servicio");
    expect((await e("pendiente", { datos: { motivo: "Falta refacción" } })).body.estado).toBe("Pendiente");
    expect((await e("en_servicio")).body.estado).toBe("En servicio");
    const sinFirma = await e("finalizar", { datos: { trabajo_realizado: "Instalada", firma_nombre: "Ana" }, adjuntos: [{ rol: "foto", base64: IMG }] });
    expect(sinFirma.body.codigo).toBe("FALTA_FIRMA");
    const fin = await e("finalizar", {
      datos: { trabajo_realizado: "Rockola instalada y probada", firma_nombre: "Ana López", materiales_utilizados: [{ descripcion: "Cable HDMI 3 m", cantidad: 1 }] },
      adjuntos: [{ rol: "foto", base64: IMG }, { rol: "foto", base64: IMG }, { rol: "firma", base64: IMG }],
    });
    expect(fin.body).toMatchObject({ ok: true, estado: "Finalizado" });
    const t = o.get("project.task", 8001)!;
    expect(t.stage_id[1]).toBe("Finalizado");
    expect(t.x_app_firma_nombre).toBe("Ana López");
    const mats = o.tabla("x_app_material");
    expect(mats.map((m) => [m.x_tipo, m.x_cantidad])).toEqual([["solicitado", 2], ["utilizado", 1]]);
    expect(o.tabla("x_app_evento").every((ev) => ev.x_resultado === "aplicado")).toBe(true);

    const despues = await e("en_camino");
    expect(despues.body.codigo).toBe("YA_CERRADA");
    const tareas = await llamar("GET", "/v1/tareas", undefined, token);
    expect(tareas.body.tareas[0].materiales).toHaveLength(2);
  });

  it("servicio cancelado desde la oficina rechaza el avance", async () => {
    o.get("project.task", 8001)!.stage_id = [66, "Cancelado"];
    const r = await llamar("POST", "/v1/eventos", { uuid: uuid(), tipo: "en_camino", tarea: { tipo: "servicio", id: 8001 }, fecha_dispositivo: new Date().toISOString() }, token);
    expect(r.body.codigo).toBe("CANCELADA");
  });
});

describe("configuración", () => {
  it("avisa si faltan secretos", async () => {
    const r = await manejar(new Request("https://p/v1/salud"), { ...env, ODOO_API_KEY: "" }, o.fetch);
    expect(r.status).toBe(500);
  });
});
