package mx.grupo.campo.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

@OptIn(ExperimentalSerializationApi::class)
val JSON = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    coerceInputValues = true
    namingStrategy = JsonNamingStrategy.SnakeCase
    encodeDefaults = true
}

@Serializable
data class Operador(
    val id: Int,
    val nombre: String,
    val rol: String, // "transportista" | "tecnico"
    val empresa: String? = null,
)

@Serializable
data class RespuestaLogin(
    val ok: Boolean = false,
    val token: String = "",
    val operador: Operador? = null,
    val debeCambiarPassword: Boolean = false,
)

@Serializable
data class Direccion(
    val nombre: String = "",
    val calle: String? = null,
    val colonia: String? = null,
    val ciudad: String? = null,
    val estado: String? = null,
    val cp: String? = null,
    val referencias: String? = null,
    val telefono: String? = null,
    val texto: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
)

@Serializable
data class Producto(
    val producto: String? = null,
    val descripcion: String? = null,
    val cantidad: Double = 0.0,
    val unidad: String? = null,
)

@Serializable
data class Material(
    val id: Int? = null,
    val uuid: String? = null,
    val descripcion: String = "",
    val producto: String? = null,
    val cantidad: Double = 0.0,
    val observaciones: String? = null,
    val tipo: String = "solicitado", // "solicitado" | "utilizado"
    val estado: String? = null,
)

@Serializable
data class SlaInfo(
    val limite: String? = null,
    val fuente: String = "",
    val pausado: Boolean = false,
    val cumplidoEn: String? = null,
)

/** Tarea tal como la entrega el puente: una entrega (stock.picking) o un servicio (project.task). */
@Serializable
data class Tarea(
    val tipo: String, // "entrega" | "servicio"
    val id: Int,
    val referencia: String = "",
    val pedido: String? = null,
    val referenciaCliente: String? = null,
    val empresa: String? = null,
    val modalidad: String? = null, // "propio" | "paqueteria"
    val estado: String = "asignado",
    val estadoOdoo: String? = null,
    val prioridad: String = "normal",
    val cliente: String? = null,
    val recibe: String? = null,
    val telefono: String? = null,
    val direccion: Direccion? = null,
    val destinoPaqueteria: String? = null,
    val remitente: Direccion? = null,
    val instrucciones: String? = null,
    val ventanaInicio: String? = null,
    val ventanaFin: String? = null,
    val productos: List<Producto> = emptyList(),
    val bultos: Int? = null,
    val pesoKg: Double? = null,
    val medidas: String? = null,
    val paqueteria: String? = null,
    val guia: String? = null,
    val costoEnvio: Double? = null,
    val moneda: String? = null,
    val motivoNoEntrega: String? = null,
    val tipoServicio: String? = null,
    val etapa: String? = null,
    val aceptadoEl: String? = null,
    val equipo: String? = null,
    val descripcion: String? = null,
    val trabajoRealizado: String? = null,
    val motivoPendiente: String? = null,
    val materiales: List<Material> = emptyList(),
    val sla: SlaInfo? = null,
    val actualizado: String? = null,
) {
    val clave: String get() = "$tipo:$id"
    val esPaqueteria: Boolean get() = tipo == "entrega" && modalidad == "paqueteria"
    val esServicio: Boolean get() = tipo == "servicio"
}

@Serializable
data class RespuestaTareas(val ok: Boolean = false, val servidor: String? = null, val tareas: List<Tarea> = emptyList())

@Serializable
data class RespuestaEvento(
    val ok: Boolean = false,
    val resultado: String? = null,
    val eventoId: Int? = null,
    val estado: String? = null,
    val codigo: String? = null,
    val mensaje: String? = null,
    val reintentar: Boolean = false,
)

@Serializable
data class ErrorPuente(val ok: Boolean = false, val codigo: String = "", val mensaje: String = "", val reintentar: Boolean = false)

/** Estados de una acción guardada en el teléfono. */
object EstadoSync {
    const val PENDIENTE = "PENDIENTE"
    const val ENVIANDO = "ENVIANDO"
    const val SINCRONIZADO = "SINCRONIZADO"
    const val ERROR = "ERROR" // Odoo o el puente rechazaron los datos; requiere corregir
    const val RECHAZADO = "RECHAZADO" // la oficina reasignó o cerró la tarea; evidencias guardadas sin cambiar estado
}

/** Estado de campo que resulta de cada acción (igual que en el puente). */
object Transiciones {
    private val entrega = mapOf(
        "iniciar_traslado" to "en_traslado",
        "entregado_cliente" to "entregado_cliente",
        "entregado_paqueteria" to "entregado_paqueteria",
        "no_entregado" to "no_entregado",
    )
    private val servicio = mapOf(
        "en_camino" to "en_camino",
        "en_servicio" to "en_servicio",
        "pendiente" to "pendiente",
        "finalizar" to "finalizado",
    )

    fun estadoTras(tipoTarea: String, accion: String): String? =
        if (tipoTarea == "entrega") entrega[accion] else servicio[accion]

    val ETIQUETA_ESTADO = mapOf(
        "asignado" to "Asignado",
        "en_traslado" to "En traslado",
        "entregado_cliente" to "Entregado al cliente",
        "entregado_paqueteria" to "Entregado a paquetería",
        "no_entregado" to "No entregado",
        "en_camino" to "En camino",
        "en_servicio" to "En servicio",
        "pendiente" to "Pendiente",
        "finalizado" to "Finalizado",
        "cancelado" to "Cancelado",
    )

    val FINALES = setOf("entregado_cliente", "entregado_paqueteria", "finalizado", "cancelado")
}
