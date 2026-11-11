// Odoo en memoria para probar el puente sin tocar la instancia real.
// Imita /jsonrpc: common.authenticate y object.execute_kw con search_read, read, write, create, unlink, message_post.

type Rec = Record<string, any> & { id: number };

export class OdooFalso {
  db: Record<string, Rec[]> = {};
  seq = 1000;
  llamadas: { model: string; method: string }[] = [];
  mensajes: { model: string; id: number; body: string; attachment_ids: number[] }[] = [];
  fallarSiguiente: string | null = null; // nombre de método que fallará una vez (error técnico)

  constructor(public readonly apiKey = "llave-secreta") {}

  tabla(model: string): Rec[] {
    return (this.db[model] ||= []);
  }

  agregar(model: string, vals: Record<string, any>): number {
    const id = vals.id ?? ++this.seq;
    this.tabla(model).push({ ...vals, id, write_date: "2026-11-10 10:00:00" });
    return id;
  }

  get(model: string, id: number): Rec | undefined {
    return this.tabla(model).find((r) => r.id === id);
  }

  private valor(model: string, r: Rec, campo: string): any {
    if (!campo.includes(".")) {
      const v = r[campo];
      return Array.isArray(v) && v.length === 2 && typeof v[0] === "number" && typeof v[1] === "string" ? v[0] : v;
    }
    const [m2o, sub] = campo.split(".");
    const v = r[m2o];
    if (!Array.isArray(v)) return undefined;
    const rel = this.relacion(model, m2o);
    const otro = rel ? this.get(rel, v[0]) : undefined;
    return otro ? otro[sub] : sub === "name" ? v[1] : undefined;
  }

  private relacion(model: string, campo: string): string | null {
    const mapa: Record<string, string> = { stage_id: "project.task.type", mail_message_id: "mail.message", field_id: "ir.model.fields" };
    return mapa[campo] || null;
  }

  private cumple(model: string, r: Rec, dominio: any[]): boolean {
    const pila: boolean[] = [];
    for (let i = dominio.length - 1; i >= 0; i--) {
      const t = dominio[i];
      if (t === "|" || t === "&") {
        const a = pila.pop()!, b = pila.pop()!; // sacar ambos siempre (sin cortocircuito)
        pila.push(t === "|" ? a || b : a && b);
      }
      else if (t === "!") pila.push(!pila.pop()!);
      else pila.push(this.hoja(model, r, t));
    }
    return pila.every(Boolean);
  }

  private hoja(model: string, r: Rec, [campo, op, val]: [string, string, any]): boolean {
    const v = this.valor(model, r, campo);
    switch (op) {
      case "=":
        return val === false ? !v : Array.isArray(v) ? v.includes(val) : v === val;
      case "!=":
        return val === false ? !!v : v !== val;
      case "in":
        return Array.isArray(v) ? v.some((x) => val.includes(x)) : val.includes(v);
      case "not in":
        return !val.includes(v);
      case ">=":
        return v >= val;
      case "=ilike":
      case "=like": {
        // Como en PostgreSQL: % = cualquier texto, _ = un carácter, \ escapa el siguiente.
        let re = "";
        const p = String(val);
        for (let i = 0; i < p.length; i++) {
          const c = p[i];
          if (c === "\\" && i + 1 < p.length) re += p[++i].replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
          else if (c === "%") re += ".*";
          else if (c === "_") re += ".";
          else re += c.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
        }
        return new RegExp("^" + re + "$", op === "=ilike" ? "i" : "").test(String(v ?? ""));
      }
      default:
        throw new Error(`operador no soportado en prueba: ${op}`);
    }
  }

  private campos(r: Rec, fields?: string[]): Rec {
    if (!fields || !fields.length) return { ...r };
    const out: Rec = { id: r.id };
    for (const f of fields) out[f] = r[f] ?? false;
    return out;
  }

  ejecutar(model: string, method: string, args: any[], kw: any): any {
    this.llamadas.push({ model, method });
    if (this.fallarSiguiente === method) {
      this.fallarSiguiente = null;
      throw Object.assign(new Error("Fallo simulado de red"), { tecnico: true });
    }
    switch (method) {
      case "search_read": {
        let rs = this.tabla(model).filter((r) => this.cumple(model, r, args[0] || []));
        if (kw.order?.startsWith("id asc")) rs = rs.sort((a, b) => a.id - b.id);
        if (kw.limit) rs = rs.slice(0, kw.limit);
        return rs.map((r) => this.campos(r, kw.fields));
      }
      case "read":
        return (args[0] as number[]).map((id) => this.get(model, id)).filter(Boolean).map((r) => this.campos(r!, kw.fields));
      case "write":
        for (const id of args[0]) {
          const r = this.get(model, id);
          if (!r) throw new Error("MissingError");
          for (const [k, v] of Object.entries(args[1] as object)) {
            if (k === "stage_id" && typeof v === "number") r[k] = [v, this.get("project.task.type", v)!.name];
            else if (k.endsWith("_id") && typeof v === "number") r[k] = [v, `#${v}`];
            else r[k] = v;
          }
          r.write_date = "2026-11-10 12:00:00";
        }
        return true;
      case "create": {
        const vals = { ...args[0] };
        for (const [k, v] of Object.entries(vals)) if (k.endsWith("_id") && !k.endsWith("res_id") && typeof v === "number") vals[k] = [v, `#${v}`];
        return this.agregar(model, vals);
      }
      case "unlink":
        this.db[model] = this.tabla(model).filter((r) => !args[0].includes(r.id));
        return true;
      case "message_post":
        this.mensajes.push({ model, id: args[0][0], body: kw.body, attachment_ids: kw.attachment_ids || [] });
        return ++this.seq;
      default:
        throw new Error(`método no soportado en prueba: ${method}`);
    }
  }

  /** fetch falso que responde como /jsonrpc. */
  fetch = async (_url: string, init?: RequestInit): Promise<Response> => {
    const { params } = JSON.parse(String(init?.body));
    const responder = (result: unknown) => new Response(JSON.stringify({ jsonrpc: "2.0", id: 1, result }), { status: 200 });
    if (params.service === "common" && params.method === "authenticate") {
      return responder(params.args[2] === this.apiKey ? 7 : false);
    }
    const [, uid, key, model, method, args, kw] = params.args;
    if (uid !== 7 || key !== this.apiKey) {
      return new Response(JSON.stringify({ error: { data: { message: "Access Denied", name: "odoo.exceptions.AccessDenied" } } }));
    }
    if (model === "res.users" && method === "read") return responder([{ id: 7, company_ids: [1, 186] }]);
    try {
      return responder(this.ejecutar(model, method, args, kw));
    } catch (e: any) {
      if (e.tecnico) return new Response("bad gateway", { status: 502 });
      return new Response(JSON.stringify({ error: { data: { message: e.message, name: "odoo.exceptions.ValidationError" } } }));
    }
  };
}

export const IMG = "iVBORw0KGgo" + "A".repeat(400); // base64 de relleno, suficientemente largo
