// Arma la lista de tareas de una persona: entregas (stock.picking) o servicios (project.task).
// Solo regresa lo asignado a esa persona; el filtro se hace en el puente, nunca en el teléfono.

import { Odoo, odooAIso } from "./odoo";
import { Operador, Rol } from "./auth";

export const ESTADOS_ENTREGA = ["asignado", "en_traslado", "entregado_cliente", "entregado_paqueteria", "no_entregado"] as const;
export const ESTADOS_FINALES_ENTREGA = ["entregado_cliente", "entregado_paqueteria"];

/** Etapas del proyecto "Servicios técnicos" (nombre en Odoo -> clave de la app). */
export const ETAPAS_SERVICIO: Record<string, string> = {
  Asignado: "asignado",
  "En camino": "en_camino",
  "En servicio": "en_servicio",
  Pendiente: "pendiente",
  Finalizado: "finalizado",
  Cancelado: "cancelado",
};

export interface Direccion {
  nombre: string;
  calle: string | null;
  colonia: string | null;
  ciudad: string | null;
  estado: string | null;
  cp: string | null;
  referencias: string | null;
  telefono: string | null;
  texto: string; // dirección completa en una línea, para navegación
  lat: number | null;
  lon: number | null;
}

export interface Sla {
  limite: string | null; // ISO UTC
  fuente: string; // de dónde salió la fecha
  pausado: boolean;
  cumplido_en: string | null; // cuándo se cumplió el evento que cierra el SLA
}

const CAMPOS_PARTNER = [
  "name", "street", "street2", "l10n_mx_edi_colony", "city", "state_id", "zip", "phone", "mobile",
  "partner_latitude", "partner_longitude", "parent_id",
];

function direccion(p: any): Direccion {
  if (!p) {
    return { nombre: "", calle: null, colonia: null, ciudad: null, estado: null, cp: null, referencias: null, telefono: null, texto: "", lat: null, lon: null };
  }
  const estado = p.state_id ? String(p.state_id[1]).replace(/\s*\(.*\)$/, "") : null;
  const partes = [p.street, p.l10n_mx_edi_colony, p.zip ? `C.P. ${p.zip}` : null, p.city, estado].filter(Boolean);
  return {
    nombre: p.name || "",
    calle: p.street || null,
    colonia: p.l10n_mx_edi_colony || null,
    ciudad: p.city || null,
    estado,
    cp: p.zip || null,
    referencias: p.street2 || null,
    telefono: p.mobile || p.phone || null,
    texto: partes.join(", "),
    lat: p.partner_latitude || null,
    lon: p.partner_longitude || null,
  };
}

function texto(html: string | false | null): string | null {
  if (!html) return null;
  const t = String(html)
    .replace(/<br\s*\/?>/gi, "\n")
    .replace(/<\/p>/gi, "\n")
    .replace(/<[^>]+>/g, "")
    .replace(/&nbsp;/g, " ")
    .replace(/&amp;/g, "&")
    .replace(/&lt;/g, "<")
    .replace(/&gt;/g, ">")
    .trim();
  return t || null;
}

function m2oNombre(v: any): string | null {
  return Array.isArray(v) ? String(v[1]) : null;
}

// ---------------- Entregas ----------------

const CAMPOS_PICKING = [
  "name", "origin", "state", "priority", "scheduled_date", "date_deadline", "date_done", "note", "partner_id", "company_id",
  "sale_id", "move_ids", "shipping_weight", "write_date",
  "x_app_modalidad", "x_app_estado", "x_app_recibe_nombre", "x_app_ventana_fin", "x_app_bultos", "x_app_medidas",
  "x_app_paqueteria", "x_app_guia", "x_app_costo_envio", "x_app_moneda_id", "x_app_factura_paqueteria",
  "x_app_fecha_confirmacion", "x_app_motivo_no_entrega", "x_app_destino_paqueteria",
];

export async function entregasDe(odoo: Odoo, op: Operador, diasTerminadas = 2): Promise<any[]> {
  const desde = new Date(Date.now() - diasTerminadas * 86400000).toISOString().slice(0, 19).replace("T", " ");
  const pickings = await odoo.searchRead(
    "stock.picking",
    [
      ["x_app_transportista_id", "=", op.id],
      ["picking_type_code", "=", "outgoing"],
      ["state", "!=", "cancel"],
      "|", ["x_app_estado", "not in", ESTADOS_FINALES_ENTREGA], ["write_date", ">=", desde],
    ],
    CAMPOS_PICKING,
    { order: "date_deadline asc, scheduled_date asc", limit: 200 },
  );
  if (!pickings.length) return [];

  const moveIds = pickings.flatMap((p) => p.move_ids as number[]);
  const partnerIds = new Set<number>();
  const saleIds = new Set<number>();
  const companyIds = new Set<number>();
  for (const p of pickings) {
    if (p.partner_id) partnerIds.add(p.partner_id[0]);
    if (p.sale_id) saleIds.add(p.sale_id[0]);
    if (p.company_id) companyIds.add(p.company_id[0]);
  }
  const moves = await odoo.read("stock.move", moveIds, ["picking_id", "product_id", "product_uom_qty", "quantity", "product_uom", "description_picking"]);
  const ventas = await odoo.read("sale.order", [...saleIds], ["name", "partner_id", "client_order_ref", "commitment_date", "user_id"]);
  const empresas = await odoo.read("res.company", [...companyIds], ["name", "partner_id"]);
  for (const v of ventas) if (v.partner_id) partnerIds.add(v.partner_id[0]);
  for (const c of empresas) if (c.partner_id) partnerIds.add(c.partner_id[0]);
  const partners = await odoo.read("res.partner", [...partnerIds], CAMPOS_PARTNER);

  const pMap = new Map(partners.map((p: any) => [p.id, p]));
  const vMap = new Map(ventas.map((v: any) => [v.id, v]));
  const cMap = new Map(empresas.map((c: any) => [c.id, c]));

  return pickings.map((p) => {
    const venta = p.sale_id ? vMap.get(p.sale_id[0]) : null;
    const empresa = p.company_id ? cMap.get(p.company_id[0]) : null;
    const destino = direccion(p.partner_id ? pMap.get(p.partner_id[0]) : null);
    const cliente = venta?.partner_id ? pMap.get(venta.partner_id[0]) : null;
    const remitente = empresa?.partner_id ? direccion(pMap.get(empresa.partner_id[0])) : null;
    const productos = moves
      .filter((m: any) => m.picking_id && m.picking_id[0] === p.id)
      .map((m: any) => ({
        producto: m2oNombre(m.product_id),
        descripcion: m.description_picking || null,
        cantidad: m.product_uom_qty,
        unidad: m2oNombre(m.product_uom),
      }));
    const estado = p.x_app_estado || "asignado";
    const limite = odooAIso(p.date_deadline) || odooAIso(venta?.commitment_date) || null;
    const sla: Sla = {
      limite,
      fuente: p.date_deadline ? "Fecha límite de la entrega" : venta?.commitment_date ? "Fecha comprometida de la venta" : "Sin fecha comprometida",
      pausado: false,
      cumplido_en: ESTADOS_FINALES_ENTREGA.includes(estado) ? odooAIso(p.x_app_fecha_confirmacion) : null,
    };
    return {
      tipo: "entrega",
      id: p.id,
      referencia: p.name,
      pedido: venta?.name || p.origin || null,
      referencia_cliente: venta?.client_order_ref || null,
      empresa: empresa?.name || null,
      modalidad: p.x_app_modalidad || "propio",
      estado,
      estado_odoo: p.state,
      prioridad: p.priority === "1" ? "urgente" : "normal",
      cliente: cliente?.name || destino.nombre,
      recibe: p.x_app_recibe_nombre || destino.nombre,
      telefono: destino.telefono,
      direccion: destino,
      destino_paqueteria: p.x_app_destino_paqueteria || null,
      remitente,
      instrucciones: texto(p.note),
      ventana_inicio: odooAIso(p.scheduled_date),
      ventana_fin: odooAIso(p.x_app_ventana_fin) || limite,
      productos,
      bultos: p.x_app_bultos || null,
      peso_kg: p.shipping_weight || null,
      medidas: p.x_app_medidas || null,
      paqueteria: p.x_app_paqueteria || null,
      guia: p.x_app_guia || null,
      costo_envio: p.x_app_costo_envio || null,
      moneda: m2oNombre(p.x_app_moneda_id),
      motivo_no_entrega: p.x_app_motivo_no_entrega || null,
      sla,
      actualizado: odooAIso(p.write_date),
    };
  });
}

// ---------------- Servicios ----------------

const CAMPOS_TASK = [
  "name", "description", "priority", "stage_id", "partner_id", "partner_phone", "company_id", "sale_order_id", "sale_line_id",
  "planned_date_begin", "date_deadline", "write_date", "project_id",
  "x_app_tipo_servicio", "x_app_equipo", "x_app_aceptado_el", "x_app_trabajo_realizado", "x_app_motivo_pendiente", "x_app_finalizado_el",
];

export async function serviciosDe(odoo: Odoo, op: Operador, diasTerminadas = 2): Promise<any[]> {
  const desde = new Date(Date.now() - diasTerminadas * 86400000).toISOString().slice(0, 19).replace("T", " ");
  const tareas = await odoo.searchRead(
    "project.task",
    [
      ["x_app_tecnico_id", "=", op.id],
      ["stage_id.name", "!=", "Cancelado"],
      "|", ["stage_id.name", "!=", "Finalizado"], ["write_date", ">=", desde],
    ],
    CAMPOS_TASK,
    { order: "date_deadline asc, planned_date_begin asc", limit: 200 },
  );
  if (!tareas.length) return [];

  const ids = tareas.map((t) => t.id);
  const materiales = await odoo.searchRead(
    "x_app_material",
    [["x_tarea_id", "in", ids]],
    ["x_tarea_id", "x_name", "x_cantidad", "x_observaciones", "x_tipo", "x_estado", "x_uuid", "x_producto_id"],
    { order: "id asc" },
  );
  const partnerIds = new Set<number>();
  const saleIds = new Set<number>();
  const lineIds = new Set<number>();
  for (const t of tareas) {
    if (t.partner_id) partnerIds.add(t.partner_id[0]);
    if (t.sale_order_id) saleIds.add(t.sale_order_id[0]);
    if (t.sale_line_id) lineIds.add(t.sale_line_id[0]);
  }
  const ventas = await odoo.read("sale.order", [...saleIds], ["name", "partner_shipping_id"]);
  for (const v of ventas) if (v.partner_shipping_id) partnerIds.add(v.partner_shipping_id[0]);
  const lineas = await odoo.read("sale.order.line", [...lineIds], ["product_id", "name"]);
  const partners = await odoo.read("res.partner", [...partnerIds], CAMPOS_PARTNER);
  const pMap = new Map(partners.map((p: any) => [p.id, p]));
  const vMap = new Map(ventas.map((v: any) => [v.id, v]));
  const lMap = new Map(lineas.map((l: any) => [l.id, l]));

  return tareas.map((t) => {
    const venta = t.sale_order_id ? vMap.get(t.sale_order_id[0]) : null;
    const lugar = venta?.partner_shipping_id ? pMap.get(venta.partner_shipping_id[0]) : t.partner_id ? pMap.get(t.partner_id[0]) : null;
    const cliente = t.partner_id ? pMap.get(t.partner_id[0]) : null;
    const etapa = m2oNombre(t.stage_id) || "Asignado";
    const estado = ETAPAS_SERVICIO[etapa] || "asignado";
    const linea = t.sale_line_id ? lMap.get(t.sale_line_id[0]) : null;
    const mats = materiales
      .filter((m: any) => m.x_tarea_id && m.x_tarea_id[0] === t.id)
      .map((m: any) => ({
        id: m.id,
        uuid: m.x_uuid || null,
        descripcion: m.x_name,
        producto: m2oNombre(m.x_producto_id),
        cantidad: m.x_cantidad,
        observaciones: m.x_observaciones || null,
        tipo: m.x_tipo,
        estado: m.x_estado || null,
      }));
    return {
      tipo: "servicio",
      id: t.id,
      referencia: `${t.name}`,
      pedido: venta?.name || null,
      empresa: m2oNombre(t.company_id),
      tipo_servicio: t.x_app_tipo_servicio || "instalacion",
      estado,
      etapa,
      aceptado_el: odooAIso(t.x_app_aceptado_el),
      prioridad: t.priority === "1" ? "urgente" : "normal",
      cliente: cliente?.name || null,
      telefono: t.partner_phone || cliente?.mobile || cliente?.phone || null,
      direccion: direccion(lugar),
      equipo: t.x_app_equipo || (linea ? m2oNombre(linea.product_id) : null),
      descripcion: texto(t.description),
      ventana_inicio: odooAIso(t.planned_date_begin),
      ventana_fin: odooAIso(t.date_deadline),
      trabajo_realizado: t.x_app_trabajo_realizado || null,
      motivo_pendiente: t.x_app_motivo_pendiente || null,
      materiales: mats,
      sla: {
        limite: odooAIso(t.date_deadline),
        fuente: t.date_deadline ? "Fecha límite de la tarea" : "Sin fecha comprometida",
        pausado: estado === "pendiente",
        cumplido_en: estado === "finalizado" ? odooAIso(t.x_app_finalizado_el) : null,
      } as Sla,
      actualizado: odooAIso(t.write_date),
    };
  });
}

export async function tareasDe(odoo: Odoo, op: Operador): Promise<any[]> {
  return op.rol === ("transportista" as Rol) ? entregasDe(odoo, op) : serviciosDe(odoo, op);
}
