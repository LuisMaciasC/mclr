// Aplica en Odoo un evento capturado en el teléfono (cambio de estado, evidencias, materiales).
// Garantías:
//  - Idempotencia: cada evento trae un folio único (uuid). Si ya se procesó, se responde lo mismo sin reescribir.
//  - Identidad: cada evento queda en la bitácora x_app_evento con la persona, la hora del teléfono y la del servidor.
//  - Nada se pierde: si la tarea fue reasignada o cancelada, el evento y sus evidencias se guardan pero no cambian el estado.

import { Odoo, isoAOdoo } from "./odoo";
import { Operador } from "./auth";
import { ESTADOS_FINALES_ENTREGA } from "./tareas";

export interface Adjunto {
  rol: "foto" | "firma" | "comprobante";
  nombre?: string;
  mimetype?: string;
  base64: string;
}

export interface Evento {
  uuid: string;
  tipo: string;
  tarea: { tipo: "entrega" | "servicio"; id: number };
  fecha_dispositivo: string; // ISO-8601
  lat?: number | null;
  lon?: number | null;
  app_version?: string;
  datos?: Record<string, any>;
  adjuntos?: Adjunto[];
}

export interface RespuestaEvento {
  ok: boolean;
  resultado: "aplicado" | "rechazado";
  evento_id: number;
  estado?: string;
  codigo?: string;
  mensaje?: string;
  repetido?: boolean;
}

export class EventoError extends Error {
  constructor(message: string, public readonly codigo: string, public readonly http = 400, public readonly reintentar = false) {
    super(message);
  }
}

const TIPOS_ENTREGA = ["iniciar_traslado", "entregado_cliente", "entregado_paqueteria", "no_entregado"];
const TIPOS_SERVICIO = ["aceptar", "en_camino", "en_servicio", "pendiente", "finalizar", "incidencia", "materiales_solicitados", "materiales_utilizados"];

const ETIQUETAS: Record<string, string> = {
  iniciar_traslado: "Inició el traslado",
  entregado_cliente: "Entregado al cliente",
  entregado_paqueteria: "Entregado a paquetería",
  no_entregado: "Entrega no realizada",
  aceptar: "Aceptó el servicio",
  en_camino: "En camino",
  en_servicio: "En servicio",
  pendiente: "Servicio pendiente",
  finalizar: "Servicio finalizado",
  incidencia: "Incidencia reportada",
  materiales_solicitados: "Solicitó materiales",
  materiales_utilizados: "Registró materiales utilizados",
};

const ETAPA_POR_TIPO: Record<string, string> = {
  en_camino: "En camino",
  en_servicio: "En servicio",
  pendiente: "Pendiente",
  finalizar: "Finalizado",
};

const MAX_ADJUNTOS = 12;
const MAX_B64 = 7_000_000; // ~5 MB por archivo

// ---------- validación de forma ----------

function req(cond: unknown, msg: string, codigo = "DATOS_INVALIDOS"): asserts cond {
  if (!cond) throw new EventoError(msg, codigo);
}

function textoOblig(v: unknown, campo: string): string {
  req(typeof v === "string" && v.trim().length > 0, `Falta ${campo}.`);
  return (v as string).trim().slice(0, 500);
}

export function validarForma(ev: Evento): void {
  req(ev && typeof ev === "object", "Evento vacío.");
  req(typeof ev.uuid === "string" && /^[0-9a-fA-F-]{16,64}$/.test(ev.uuid), "Folio (uuid) inválido.");
  req(ev.tarea && (ev.tarea.tipo === "entrega" || ev.tarea.tipo === "servicio") && Number.isInteger(ev.tarea.id), "Tarea inválida.");
  const permitidos = ev.tarea.tipo === "entrega" ? TIPOS_ENTREGA : TIPOS_SERVICIO;
  req(permitidos.includes(ev.tipo), `Acción no válida para ${ev.tarea.tipo}: ${ev.tipo}.`);
  req(!isNaN(Date.parse(ev.fecha_dispositivo)), "Fecha del teléfono inválida.");
  const adj = ev.adjuntos || [];
  req(Array.isArray(adj) && adj.length <= MAX_ADJUNTOS, `Máximo ${MAX_ADJUNTOS} archivos por acción.`);
  for (const a of adj) {
    req(["foto", "firma", "comprobante"].includes(a.rol), "Tipo de archivo inválido.");
    req(typeof a.base64 === "string" && a.base64.length > 100 && a.base64.length < MAX_B64, "Archivo vacío o demasiado grande.");
    // Solo imágenes reales: JPEG (/9j/) o PNG (iVBORw0KGgo). Evita subir HTML u otros archivos disfrazados.
    req(a.base64.startsWith("/9j/") || a.base64.startsWith("iVBORw0KGgo"), "Solo se aceptan fotos JPEG o PNG.", "ARCHIVO_INVALIDO");
  }
  const d = ev.datos || {};
  const cuenta = (rol: string) => adj.filter((a) => a.rol === rol).length;
  switch (ev.tipo) {
    case "entregado_cliente":
      req(cuenta("foto") >= 1, "La foto de evidencia es obligatoria.", "FALTA_FOTO");
      req(cuenta("firma") === 1, "La firma de quien recibe es obligatoria.", "FALTA_FIRMA");
      textoOblig(d.recibe_nombre, "el nombre de quien recibió");
      break;
    case "entregado_paqueteria":
      textoOblig(d.paqueteria, "la paquetería");
      textoOblig(d.guia, "el número de guía");
      req(typeof d.precio === "number" && d.precio >= 0 && d.precio < 1_000_000, "Precio inválido.");
      req(typeof d.moneda === "string" && /^[A-Z]{3}$/.test(d.moneda), "Moneda inválida.");
      req(!isNaN(Date.parse(d.fecha_entrega)), "Fecha de entrega a paquetería inválida.");
      req(cuenta("comprobante") >= 1, "La foto de la nota o recibo es obligatoria.", "FALTA_COMPROBANTE");
      break;
    case "no_entregado":
      textoOblig(d.motivo, "el motivo");
      break;
    case "pendiente":
    case "incidencia":
      textoOblig(d.motivo, "el motivo");
      break;
    case "finalizar":
      textoOblig(d.trabajo_realizado, "el trabajo realizado");
      textoOblig(d.firma_nombre, "el nombre de quien firma");
      req(cuenta("firma") === 1, "La firma del cliente es obligatoria.", "FALTA_FIRMA");
      req(cuenta("foto") >= 1, "Agrega al menos una foto del trabajo.", "FALTA_FOTO");
      validarMateriales(d.materiales_utilizados || [], false);
      break;
    case "materiales_solicitados":
    case "materiales_utilizados":
      validarMateriales(d.materiales, true);
      break;
  }
}

function validarMateriales(lista: unknown, obligatorio: boolean): void {
  req(Array.isArray(lista), "Lista de materiales inválida.");
  if (obligatorio) req((lista as unknown[]).length > 0, "Agrega al menos un material.");
  req((lista as unknown[]).length <= 50, "Máximo 50 materiales por envío.");
  for (const m of lista as any[]) {
    textoOblig(m?.descripcion, "la descripción del material");
    req(typeof m.cantidad === "number" && m.cantidad > 0, "Cantidad de material inválida.");
  }
}

// ---------- utilidades ----------

function esc(s: unknown): string {
  return String(s ?? "").replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function horaLocal(iso: string): string {
  return new Date(iso).toLocaleString("es-MX", { timeZone: "America/Mexico_City", dateStyle: "short", timeStyle: "short" });
}

const MODELO = { entrega: "stock.picking", servicio: "project.task" } as const;
const CAMPO_ASIGNADO = { entrega: "x_app_transportista_id", servicio: "x_app_tecnico_id" } as const;

/**
 * La persona solo puede registrar algo (ni siquiera fotos) en tareas que tiene o tuvo asignadas.
 * "Tuvo" se comprueba con el historial de cambios de Odoo (mail.tracking.value), así una tarea
 * reasignada mientras estaba sin señal conserva sus evidencias, pero nadie puede escribir en tareas ajenas.
 */
async function verificarVinculo(odoo: Odoo, op: Operador, ev: Evento): Promise<void> {
  const modelo = MODELO[ev.tarea.tipo];
  const campo = CAMPO_ASIGNADO[ev.tarea.tipo];
  const [r] = await odoo.read(modelo, [ev.tarea.id], [campo]);
  if (!r) throw new EventoError("La tarea no existe.", "NO_EXISTE", 404);
  if (r[campo] && r[campo][0] === op.id) return;
  const historial = await odoo.searchRead(
    "mail.tracking.value",
    [
      ["mail_message_id.model", "=", modelo],
      ["mail_message_id.res_id", "=", ev.tarea.id],
      ["field_id.name", "=", campo],
      "|", ["old_value_integer", "=", op.id], ["new_value_integer", "=", op.id],
    ],
    ["id"],
    { limit: 1 },
  );
  if (!historial.length) throw new EventoError("Esta tarea nunca estuvo asignada a ti.", "SIN_ACCESO", 403);
}

// ---------- proceso principal ----------

export async function procesarEvento(odoo: Odoo, op: Operador, ev: Evento): Promise<RespuestaEvento> {
  validarForma(ev);
  if (op.rol === "transportista") req(ev.tarea.tipo === "entrega", "Tu perfil solo maneja entregas.", "PERFIL");
  if (op.rol === "tecnico") req(ev.tarea.tipo === "servicio", "Tu perfil solo maneja servicios.", "PERFIL");
  await verificarVinculo(odoo, op, ev);

  // 1) ¿Ya existe este folio?
  const previos = await odoo.searchRead("x_app_evento", [["x_uuid", "=", ev.uuid]], ["id", "x_resultado", "x_respuesta", "x_operador_id"], {
    order: "id asc",
  });
  let eventoId: number;
  let reanudando = false;
  if (previos.length) {
    const p = previos[0];
    if (p.x_operador_id && p.x_operador_id[0] !== op.id) throw new EventoError("Folio repetido de otra persona.", "FOLIO_AJENO", 409);
    if (p.x_resultado !== "en_proceso" && p.x_respuesta) {
      return { ...(JSON.parse(p.x_respuesta) as RespuestaEvento), repetido: true };
    }
    eventoId = p.id; // quedó a medias: se reanuda (las escrituras son idempotentes)
    reanudando = true;
  } else {
    eventoId = await odoo.create("x_app_evento", {
      x_name: `${ETIQUETAS[ev.tipo]} · ${op.nombre}`,
      x_uuid: ev.uuid,
      x_operador_id: op.id,
      x_rol: op.rol,
      x_modelo: MODELO[ev.tarea.tipo],
      x_res_id: ev.tarea.id,
      x_tipo: ev.tipo,
      x_fecha_dispositivo: isoAOdoo(ev.fecha_dispositivo),
      x_fecha_servidor: isoAOdoo(new Date()),
      x_lat: ev.lat ?? false,
      x_lon: ev.lon ?? false,
      x_datos: JSON.stringify(ev.datos || {}),
      x_app_version: ev.app_version || false,
      x_resultado: "en_proceso",
    });
    // Carrera: si dos reintentos llegaron juntos, se queda el de id menor.
    const mismos = await odoo.searchRead("x_app_evento", [["x_uuid", "=", ev.uuid]], ["id"], { order: "id asc" });
    if (mismos.length > 1 && mismos[0].id !== eventoId) {
      await odoo.call("x_app_evento", "unlink", [[eventoId]]);
      throw new EventoError("Esta acción ya se está guardando; se reintentará.", "EN_PROCESO", 409, true);
    }
  }

  const modelo = MODELO[ev.tarea.tipo];
  // 2) Evidencias primero: aunque la acción se rechace, las fotos y firmas quedan en Odoo.
  const adjuntoIds = await subirAdjuntos(odoo, modelo, ev);

  // 3) Revisar que la tarea siga asignada y abierta, y aplicar.
  let respuesta: RespuestaEvento;
  let estadoAnterior = "";
  try {
    const r = ev.tarea.tipo === "entrega" ? await aplicarEntrega(odoo, op, ev, reanudando) : await aplicarServicio(odoo, op, ev, reanudando);
    estadoAnterior = r.anterior;
    respuesta = { ok: true, resultado: "aplicado", evento_id: eventoId, estado: r.nuevo };
  } catch (e) {
    if (e instanceof EventoError && e.http === 409 && !e.reintentar) {
      respuesta = { ok: false, resultado: "rechazado", evento_id: eventoId, codigo: e.codigo, mensaje: e.message };
    } else {
      throw e; // error técnico: el teléfono reintentará con el mismo folio
    }
  }

  // 4) Nota en el chatter del registro con quién, qué y cuándo.
  await notaChatter(odoo, modelo, ev, op, respuesta, adjuntoIds);

  // 5) Cerrar el evento en la bitácora.
  await odoo.write("x_app_evento", [eventoId], {
    x_resultado: respuesta.resultado,
    x_estado_anterior: estadoAnterior || false,
    x_estado_nuevo: respuesta.estado || false,
    x_motivo_rechazo: respuesta.mensaje || false,
    x_adjuntos: adjuntoIds.length,
    x_respuesta: JSON.stringify(respuesta),
  });
  return respuesta;
}

async function subirAdjuntos(odoo: Odoo, modelo: string, ev: Evento): Promise<number[]> {
  const adj = ev.adjuntos || [];
  if (!adj.length) return [];
  const prefijo = `app-${ev.uuid.slice(0, 8)}`;
  const existentes = await odoo.searchRead(
    "ir.attachment",
    [["res_model", "=", modelo], ["res_id", "=", ev.tarea.id], ["name", "=like", `${prefijo}-%`]],
    ["id", "name"],
  );
  const porNombre = new Map(existentes.map((a: any) => [a.name, a.id]));
  const ids: number[] = [];
  for (let i = 0; i < adj.length; i++) {
    const a = adj[i];
    // El tipo lo decide el contenido, nunca lo que diga el teléfono.
    const ext = a.base64.startsWith("iVBORw0KGgo") ? "png" : "jpg";
    const nombre = `${prefijo}-${a.rol}-${i + 1}.${ext}`;
    const ya = porNombre.get(nombre);
    if (ya) {
      ids.push(ya);
      continue;
    }
    ids.push(
      await odoo.create("ir.attachment", {
        name: nombre,
        datas: a.base64,
        res_model: modelo,
        res_id: ev.tarea.id,
        mimetype: ext === "png" ? "image/png" : "image/jpeg",
        description: `${ETIQUETAS[ev.tipo]} (app de campo)`,
      }),
    );
  }
  return ids;
}

async function notaChatter(odoo: Odoo, modelo: string, ev: Evento, op: Operador, r: RespuestaEvento, adjuntos: number[]) {
  const d = ev.datos || {};
  const filas: string[] = [];
  const add = (k: string, v: unknown) => {
    if (v !== undefined && v !== null && v !== "") filas.push(`<li><b>${esc(k)}:</b> ${esc(v)}</li>`);
  };
  add("Recibió", d.recibe_nombre);
  add("Paquetería", d.paqueteria);
  add("Guía", d.guia);
  if (typeof d.precio === "number") add("Precio pagado", `${d.precio.toFixed(2)} ${d.moneda || ""}`);
  add("Factura paquetería", d.factura);
  if (d.fecha_entrega) add("Entregado a paquetería", horaLocal(d.fecha_entrega));
  add("Motivo", d.motivo);
  add("Observaciones", d.observaciones);
  add("Trabajo realizado", d.trabajo_realizado);
  add("Firmó", d.firma_nombre);
  const mats = (d.materiales || d.materiales_utilizados || []) as any[];
  if (mats.length) add("Materiales", mats.map((m) => `${m.cantidad} × ${m.descripcion}`).join("; "));
  add("Hora del teléfono", horaLocal(ev.fecha_dispositivo));
  if (ev.lat != null && ev.lon != null) add("Ubicación", `${ev.lat.toFixed(5)}, ${ev.lon.toFixed(5)}`);
  const titulo = r.resultado === "aplicado"
    ? `📱 App de campo · <b>${esc(op.nombre)}</b>: ${esc(ETIQUETAS[ev.tipo])}`
    : `📱 App de campo · <b>${esc(op.nombre)}</b> intentó «${esc(ETIQUETAS[ev.tipo])}», no se aplicó: ${esc(r.mensaje)}`;
  await odoo.call(modelo, "message_post", [[ev.tarea.id]], {
    body: `<p>${titulo}</p><ul>${filas.join("")}</ul><p><small>Folio ${esc(ev.uuid)}</small></p>`,
    message_type: "comment",
    subtype_xmlid: "mail.mt_note",
    attachment_ids: adjuntos,
  });
}

// ---------- entregas ----------

const DESTINO_ENTREGA: Record<string, string> = {
  iniciar_traslado: "en_traslado",
  entregado_cliente: "entregado_cliente",
  entregado_paqueteria: "entregado_paqueteria",
  no_entregado: "no_entregado",
};

async function aplicarEntrega(odoo: Odoo, op: Operador, ev: Evento, reanudando = false): Promise<{ anterior: string; nuevo: string }> {
  const [p] = await odoo.read("stock.picking", [ev.tarea.id], ["state", "x_app_transportista_id", "x_app_estado", "x_app_modalidad", "sale_id"]);
  if (!p) throw new EventoError("La entrega ya no existe en Odoo.", "NO_EXISTE", 409);
  if (!p.x_app_transportista_id || p.x_app_transportista_id[0] !== op.id)
    throw new EventoError("La entrega fue reasignada a otra persona desde la oficina.", "REASIGNADA", 409);
  if (p.state === "cancel") throw new EventoError("La entrega fue cancelada desde la oficina.", "CANCELADA", 409);
  const anterior: string = p.x_app_estado || "asignado";
  // Reintento de un evento que ya había escrito su estado antes de cortarse: no se vuelve a aplicar.
  if (reanudando && anterior === DESTINO_ENTREGA[ev.tipo]) return { anterior, nuevo: anterior };
  if (ESTADOS_FINALES_ENTREGA.includes(anterior) || p.state === "done")
    throw new EventoError("La entrega ya estaba cerrada.", "YA_CERRADA", 409);

  const d = ev.datos || {};
  const adj = ev.adjuntos || [];
  const vals: Record<string, unknown> = {};
  let nuevo = anterior;
  switch (ev.tipo) {
    case "iniciar_traslado":
      nuevo = "en_traslado";
      vals.x_app_inicio_traslado = isoAOdoo(ev.fecha_dispositivo);
      break;
    case "entregado_cliente":
      nuevo = "entregado_cliente";
      vals.x_app_recibe_nombre = String(d.recibe_nombre).trim();
      vals.x_app_fecha_confirmacion = isoAOdoo(ev.fecha_dispositivo);
      vals.signature = adj.find((a) => a.rol === "firma")!.base64;
      break;
    case "entregado_paqueteria": {
      nuevo = "entregado_paqueteria";
      const monedas = await odoo.searchRead("res.currency", [["name", "=", d.moneda]], ["id"], { limit: 1, context: { active_test: false } });
      if (!monedas.length) throw new EventoError(`Moneda ${d.moneda} no existe en Odoo.`, "MONEDA", 400);
      Object.assign(vals, {
        x_app_paqueteria: String(d.paqueteria).trim(),
        x_app_guia: String(d.guia).trim(),
        x_app_costo_envio: d.precio,
        x_app_moneda_id: monedas[0].id,
        x_app_factura_paqueteria: d.factura ? String(d.factura).trim() : false,
        x_app_fecha_confirmacion: isoAOdoo(d.fecha_entrega),
      });
      break;
    }
    case "no_entregado":
      nuevo = "no_entregado";
      vals.x_app_motivo_no_entrega = String(d.motivo).trim() + (d.observaciones ? ` — ${String(d.observaciones).trim()}` : "");
      break;
  }
  vals.x_app_estado = nuevo;
  await odoo.write("stock.picking", [p.id], vals);
  if (nuevo === "entregado_cliente" && p.sale_id) await actualizarVenta(odoo, p.sale_id[0]);
  return { anterior, nuevo };
}

/** Pone «Estado de la entrega» de la venta en Entregado / Parcial según sus entregas (campo Studio existente). */
async function actualizarVenta(odoo: Odoo, saleId: number): Promise<void> {
  const hermanas = await odoo.searchRead(
    "stock.picking",
    [["sale_id", "=", saleId], ["picking_type_code", "=", "outgoing"], ["state", "!=", "cancel"]],
    ["state", "x_app_estado"],
  );
  const listas = hermanas.filter((h: any) => h.state === "done" || h.x_app_estado === "entregado_cliente").length;
  const valor = listas === hermanas.length ? "full" : listas > 0 ? "partial" : "pending";
  await odoo.write("sale.order", [saleId], { x_studio_estado_de_la_entrega: valor });
}

// ---------- servicios ----------

async function etapaId(odoo: Odoo, projectId: number, nombre: string): Promise<number> {
  const r = await odoo.searchRead("project.task.type", [["project_ids", "in", [projectId]], ["name", "=", nombre]], ["id"], { limit: 1 });
  if (!r.length) throw new EventoError(`No existe la etapa «${nombre}» en el proyecto de servicios.`, "ETAPA", 500, false);
  return r[0].id;
}

async function aplicarServicio(odoo: Odoo, op: Operador, ev: Evento, reanudando = false): Promise<{ anterior: string; nuevo: string }> {
  const [t] = await odoo.read("project.task", [ev.tarea.id], ["x_app_tecnico_id", "stage_id", "project_id", "x_app_aceptado_el"]);
  if (!t) throw new EventoError("El servicio ya no existe en Odoo.", "NO_EXISTE", 409);
  if (!t.x_app_tecnico_id || t.x_app_tecnico_id[0] !== op.id)
    throw new EventoError("El servicio fue reasignado a otra persona desde la oficina.", "REASIGNADA", 409);
  const anterior: string = t.stage_id ? String(t.stage_id[1]) : "";
  if (reanudando && ETAPA_POR_TIPO[ev.tipo] === anterior) return { anterior, nuevo: anterior };
  if (anterior === "Cancelado") throw new EventoError("El servicio fue cancelado desde la oficina.", "CANCELADA", 409);
  const soloRegistro = ["incidencia", "materiales_solicitados", "materiales_utilizados"];
  if (anterior === "Finalizado" && !soloRegistro.includes(ev.tipo))
    throw new EventoError("El servicio ya estaba finalizado.", "YA_CERRADA", 409);

  const d = ev.datos || {};
  const vals: Record<string, unknown> = {};
  let nuevo = anterior;
  if (ev.tipo === "aceptar") {
    vals.x_app_aceptado_el = isoAOdoo(ev.fecha_dispositivo);
  } else if (ETAPA_POR_TIPO[ev.tipo]) {
    nuevo = ETAPA_POR_TIPO[ev.tipo];
    vals.stage_id = await etapaId(odoo, t.project_id[0], nuevo);
    if (!t.x_app_aceptado_el) vals.x_app_aceptado_el = isoAOdoo(ev.fecha_dispositivo); // avanzar implica aceptar
  }
  if (ev.tipo === "pendiente") vals.x_app_motivo_pendiente = String(d.motivo).trim();
  if (ev.tipo === "finalizar") {
    Object.assign(vals, {
      x_app_trabajo_realizado: String(d.trabajo_realizado).trim(),
      x_app_firma: ev.adjuntos!.find((a) => a.rol === "firma")!.base64,
      x_app_firma_nombre: String(d.firma_nombre).trim(),
      x_app_finalizado_el: isoAOdoo(ev.fecha_dispositivo),
      x_app_motivo_pendiente: false,
    });
  }
  if (Object.keys(vals).length) await odoo.write("project.task", [t.id], vals);

  const mats: any[] =
    ev.tipo === "materiales_solicitados" || ev.tipo === "materiales_utilizados" ? d.materiales : ev.tipo === "finalizar" ? d.materiales_utilizados || [] : [];
  const tipoMat = ev.tipo === "materiales_solicitados" ? "solicitado" : "utilizado";
  for (let i = 0; i < mats.length; i++) {
    const m = mats[i];
    const folio = m.uuid || `${ev.uuid}-${i}`;
    const ya = await odoo.searchRead("x_app_material", [["x_uuid", "=", folio]], ["id"], { limit: 1 });
    if (ya.length) continue;
    await odoo.create("x_app_material", {
      x_name: String(m.descripcion).trim().slice(0, 200),
      x_tarea_id: t.id,
      x_cantidad: m.cantidad,
      x_observaciones: m.observaciones ? String(m.observaciones).trim() : false,
      x_tipo: tipoMat,
      x_estado: tipoMat === "solicitado" ? "solicitado" : false,
      x_uuid: folio,
      x_operador_id: op.id,
    });
  }
  return { anterior, nuevo };
}
