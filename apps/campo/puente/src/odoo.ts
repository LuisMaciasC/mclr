// Cliente JSON-RPC mínimo para Odoo 18 (Odoo Online).
// Las credenciales llegan solo por variables de entorno del puente (secretos de Cloudflare);
// nunca viajan al teléfono.

export interface OdooEnv {
  ODOO_URL: string; // p. ej. https://bering.odoo.com
  ODOO_DB: string; // nombre de la base
  ODOO_LOGIN: string; // usuario interno dueño de la llave
  ODOO_API_KEY: string; // llave de API "App de campo" (secreto)
}

export class OdooError extends Error {
  constructor(message: string, public readonly detalle?: unknown) {
    super(message);
    this.name = "OdooError";
  }
}

type FetchLike = (input: string, init?: RequestInit) => Promise<Response>;

export class Odoo {
  private uid: number | null = null;
  private companias: number[] | null = null;
  private cola: Promise<unknown> = Promise.resolve();

  constructor(private readonly env: OdooEnv, private readonly fetchImpl: FetchLike = fetch) {}

  private async rpc(service: string, method: string, args: unknown[]): Promise<any> {
    const resp = await this.fetchImpl(`${this.env.ODOO_URL.replace(/\/$/, "")}/jsonrpc`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ jsonrpc: "2.0", method: "call", id: Date.now(), params: { service, method, args } }),
    });
    if (!resp.ok) throw new OdooError(`Odoo respondió HTTP ${resp.status}`);
    const json: any = await resp.json();
    if (json.error) {
      const msg = json.error?.data?.message || json.error?.message || "Error de Odoo";
      throw new OdooError(msg, json.error?.data?.name);
    }
    return json.result;
  }

  async autenticar(): Promise<number> {
    if (this.uid) return this.uid;
    const uid = await this.rpc("common", "authenticate", [this.env.ODOO_DB, this.env.ODOO_LOGIN, this.env.ODOO_API_KEY, {}]);
    if (!uid) throw new OdooError("No se pudo autenticar el puente con Odoo (revisa usuario y llave)");
    this.uid = uid as number;
    return this.uid;
  }

  /** Empresas permitidas del usuario de integración: se mandan en el contexto para ver todas. */
  private async contexto(): Promise<Record<string, unknown>> {
    if (!this.companias) {
      const uid = await this.autenticar();
      const [u] = await this.rpc("object", "execute_kw", [
        this.env.ODOO_DB, uid, this.env.ODOO_API_KEY, "res.users", "read", [[uid]], { fields: ["company_ids"] },
      ]);
      this.companias = (u?.company_ids as number[]) || [];
    }
    return { allowed_company_ids: this.companias, tz: "America/Mexico_City", lang: "es_MX" };
  }

  /** Llama un método del ORM. Las llamadas se encadenan una por una (Odoo Online limita la concurrencia). */
  call<T = any>(model: string, method: string, args: unknown[] = [], kwargs: Record<string, unknown> = {}): Promise<T> {
    const tarea = this.cola.then(async () => {
      const uid = await this.autenticar();
      const context = { ...(await this.contexto()), ...((kwargs.context as object) || {}) };
      return this.rpc("object", "execute_kw", [
        this.env.ODOO_DB, uid, this.env.ODOO_API_KEY, model, method, args, { ...kwargs, context },
      ]);
    });
    this.cola = tarea.catch(() => undefined);
    return tarea as Promise<T>;
  }

  searchRead<T = any>(model: string, domain: unknown[], fields: string[], extra: Record<string, unknown> = {}): Promise<T[]> {
    return this.call<T[]>(model, "search_read", [domain], { fields, ...extra });
  }

  read<T = any>(model: string, ids: number[], fields: string[]): Promise<T[]> {
    if (!ids.length) return Promise.resolve([]);
    return this.call<T[]>(model, "read", [ids], { fields });
  }

  write(model: string, ids: number[], vals: Record<string, unknown>): Promise<boolean> {
    return this.call<boolean>(model, "write", [ids, vals]);
  }

  async create(model: string, vals: Record<string, unknown>): Promise<number> {
    const r = await this.call<number | number[]>(model, "create", [vals]);
    return Array.isArray(r) ? r[0] : r;
  }
}

/** Fecha de Odoo ("YYYY-MM-DD HH:MM:SS", UTC) a ISO-8601. */
export function odooAIso(v: string | false | null | undefined): string | null {
  if (!v) return null;
  return v.includes("T") ? v : v.replace(" ", "T") + "Z";
}

/** ISO-8601 a formato de Odoo en UTC. */
export function isoAOdoo(v: string | Date): string {
  const d = typeof v === "string" ? new Date(v) : v;
  if (isNaN(d.getTime())) throw new OdooError(`Fecha inválida: ${v}`);
  return d.toISOString().slice(0, 19).replace("T", " ");
}
