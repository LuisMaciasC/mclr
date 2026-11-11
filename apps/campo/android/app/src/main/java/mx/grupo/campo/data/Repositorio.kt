package mx.grupo.campo.data

import android.content.Context
import android.util.Base64
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mx.grupo.campo.BuildConfig
import mx.grupo.campo.sync.SyncWorker
import java.io.File
import java.time.Instant
import java.util.UUID
import java.util.concurrent.TimeUnit

/** Tarea lista para mostrar: datos de Odoo + estado local de las acciones aún no confirmadas. */
data class TareaUi(
    val tarea: Tarea,
    val estadoLocal: String,
    val sync: String?, // null = sin acciones; si no, el peor estado entre sus acciones
    val pendientes: Int,
    val cambioOficina: Boolean,
    val retirada: Boolean,
    val cumplidoLocal: Instant?,
    val eventos: List<EventoEntity>,
)

private val LISTA_ADJ = ListSerializer(AdjuntoLocal.serializer())

class Repositorio(private val ctx: Context, val sesion: Sesion, val dao: CampoDao, private val api: Api) {

    // ------------- sesión -------------

    suspend fun entrar(usuario: String, password: String): RespuestaLogin = withContext(Dispatchers.IO) {
        val r = api.login(usuario.trim(), password)
        sesion.guardar(usuario.trim(), r)
        sincronizar()
        r
    }

    suspend fun cambiarPassword(nueva: String): RespuestaLogin = withContext(Dispatchers.IO) {
        val r = api.cambiarPassword(sesion.usuario, nueva)
        sesion.guardar(sesion.usuario, r)
        r
    }

    /** Si quedaron acciones de otra persona sin enviar en este teléfono, se avisa en el inicio de sesión. */
    suspend fun pendientesDeOtrasPersonas(): Int = withContext(Dispatchers.IO) {
        val op = sesion.operador.value?.id ?: -1
        dao.pendientesDeOtros(op)
    }

    // ------------- tareas -------------

    @OptIn(ExperimentalCoroutinesApi::class)
    fun tareasUi(): Flow<List<TareaUi>> = sesion.operador.flatMapLatest { op ->
        if (op == null) flowOf(emptyList())
        else combine(dao.tareas(op.id), dao.eventos(op.id)) { tareas, eventos -> armar(tareas, eventos) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun tareaUi(clave: String): Flow<TareaUi?> =
        combine(dao.tarea(clave), dao.eventosDeTarea(clave)) { t, ev -> t?.let { armar(listOf(it), ev.reversed()).firstOrNull() } }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun eventosUi(): Flow<List<EventoEntity>> = sesion.operador.flatMapLatest { op ->
        if (op == null) flowOf(emptyList()) else dao.eventos(op.id)
    }

    private fun armar(tareas: List<TareaEntity>, eventos: List<EventoEntity>): List<TareaUi> {
        val porTarea = eventos.groupBy { it.tareaClave }
        return tareas.mapNotNull { e ->
            val t = runCatching { JSON.decodeFromString(Tarea.serializer(), e.json) }.getOrNull() ?: return@mapNotNull null
            val evs = (porTarea[t.clave] ?: emptyList()).sortedBy { it.creadoMs }
            // Estado mostrado: el que deja la última acción local aún no confirmada; si no hay, el de Odoo.
            val vigentes = evs.filter { it.estado == EstadoSync.PENDIENTE || it.estado == EstadoSync.ENVIANDO || it.estado == EstadoSync.ERROR }
            val estadoLocal = vigentes.mapNotNull { Transiciones.estadoTras(t.tipo, it.tipo) }.lastOrNull() ?: t.estado
            val cierre = vigentes.lastOrNull { Transiciones.estadoTras(t.tipo, it.tipo) in Transiciones.FINALES }
            val cumplido = cierre?.let {
                val d = runCatching { JSON.decodeFromString(JsonObject.serializer(), it.datosJson) }.getOrNull()
                val f = (d?.get("fecha_entrega") as? kotlinx.serialization.json.JsonPrimitive)?.content ?: it.fechaDispositivo
                SlaCalc.parse(f)
            }
            val sync = when {
                evs.any { it.estado == EstadoSync.ERROR } -> EstadoSync.ERROR
                vigentes.isNotEmpty() -> EstadoSync.PENDIENTE
                evs.any { it.estado == EstadoSync.RECHAZADO } -> EstadoSync.RECHAZADO
                evs.isNotEmpty() -> EstadoSync.SINCRONIZADO
                else -> null
            }
            TareaUi(t, estadoLocal, sync, vigentes.size, e.cambioOficina, e.retirada, cumplido, evs)
        }.sortedBy { SlaCalc.prioridadOrden(it.tarea, it.estadoLocal) }
    }

    private fun huella(t: Tarea): String = listOf(
        t.direccion?.texto, t.direccion?.referencias, t.ventanaFin, t.instrucciones, t.descripcion, t.modalidad, t.telefono, t.recibe,
    ).joinToString("|") { it ?: "" }

    /** Trae las tareas del puente y aplica reasignaciones/cancelaciones hechas en Odoo. */
    suspend fun refrescar(): Int = withContext(Dispatchers.IO) {
        val op = sesion.operador.value ?: return@withContext 0
        val r = try {
            api.tareas()
        } catch (e: PuenteException) {
            if (e.http == 401) sesion.marcarExpirada()
            throw e
        }
        val actuales = dao.tareasAhora(op.id).associateBy { it.clave }
        val nuevas = r.tareas.map { t ->
            val previa = actuales[t.clave]
            val h = huella(t)
            TareaEntity(
                clave = t.clave,
                operadorId = op.id,
                json = JSON.encodeToString(Tarea.serializer(), t),
                huella = h,
                cambioOficina = (previa != null && previa.huella != h) || (previa?.cambioOficina == true),
                retirada = false,
            )
        }
        val vigentes = nuevas.map { it.clave }.toSet()
        val fuera = actuales.keys - vigentes
        val conPendientes = fuera.filter { dao.abiertosDeTarea(it) > 0 }
        dao.reemplazarTareas(op.id, nuevas, conPendientes, fuera.filter { it !in conPendientes })
        dao.limpiarViejos(System.currentTimeMillis() - 7L * 86_400_000)
        fuera.size
    }

    suspend fun marcarVista(clave: String) = dao.marcarVista(clave)

    // ------------- acciones -------------

    /**
     * Guarda una acción en el teléfono (con su folio único) y programa el envío.
     * Las evidencias se quedan en almacenamiento privado hasta que Odoo confirma.
     */
    suspend fun registrar(t: Tarea, tipo: String, datos: JsonObject = JsonObject(emptyMap()), adjuntos: List<AdjuntoLocal> = emptyList()) =
        withContext(Dispatchers.IO) {
            val op = sesion.operador.value ?: error("Sin sesión")
            val loc = Ubicacion.ultima(ctx)
            dao.insertarEvento(
                EventoEntity(
                    uuid = UUID.randomUUID().toString(),
                    operadorId = op.id,
                    tareaClave = t.clave,
                    tareaTipo = t.tipo,
                    tareaId = t.id,
                    tipo = tipo,
                    datosJson = datos.toString(),
                    adjuntosJson = JSON.encodeToString(LISTA_ADJ, adjuntos),
                    fechaDispositivo = Instant.now().toString(),
                    lat = loc?.latitude,
                    lon = loc?.longitude,
                ),
            )
            sincronizar()
        }

    suspend fun reintentar(uuid: String) {
        dao.reintentar(uuid); sincronizar()
    }

    suspend fun descartar(e: EventoEntity) {
        Evidencias.borrar(JSON.decodeFromString(LISTA_ADJ, e.adjuntosJson).map { it.ruta })
        dao.descartar(e.uuid)
    }

    fun sincronizar() {
        val req = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(ctx).enqueueUniqueWork("sync", ExistingWorkPolicy.APPEND_OR_REPLACE, req)
    }

    fun programarPeriodico() {
        val req = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .build()
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork("sync-periodico", ExistingPeriodicWorkPolicy.KEEP, req)
    }

    // ------------- envío (lo usa SyncWorker) -------------

    sealed interface ResultadoEnvio {
        data object Listo : ResultadoEnvio
        data object Reintentar : ResultadoEnvio
        data object SinSesion : ResultadoEnvio
    }

    /** Envía en orden las acciones de la persona con sesión. Se detiene al primer error técnico para conservar el orden. */
    suspend fun enviarPendientes(): ResultadoEnvio = withContext(Dispatchers.IO) {
        val op = sesion.operador.value ?: return@withContext ResultadoEnvio.SinSesion
        if (sesion.token == null || sesion.debeCambiar) return@withContext ResultadoEnvio.SinSesion
        for (e in dao.porEnviar(op.id)) {
            dao.actualizarEvento(e.uuid, EstadoSync.ENVIANDO, null, 0, null, null)
            try {
                val r = api.enviarEvento(cuerpoEvento(e))
                val adj = JSON.decodeFromString(LISTA_ADJ, e.adjuntosJson)
                if (r.resultado == "aplicado") {
                    dao.actualizarEvento(e.uuid, EstadoSync.SINCRONIZADO, null, 1, System.currentTimeMillis(), r.eventoId)
                } else {
                    dao.actualizarEvento(e.uuid, EstadoSync.RECHAZADO, r.mensaje ?: "Rechazado por la oficina", 1, System.currentTimeMillis(), r.eventoId)
                }
                Evidencias.borrar(adj.map { it.ruta }) // ya están en Odoo
            } catch (ex: PuenteException) {
                when {
                    ex.http == 401 -> {
                        dao.actualizarEvento(e.uuid, EstadoSync.PENDIENTE, "Vuelve a iniciar sesión para enviar", 1, null, null)
                        sesion.marcarExpirada()
                        return@withContext ResultadoEnvio.SinSesion
                    }
                    ex.reintentar -> {
                        dao.actualizarEvento(e.uuid, EstadoSync.PENDIENTE, ex.message, 1, null, null)
                        return@withContext ResultadoEnvio.Reintentar
                    }
                    else -> dao.actualizarEvento(e.uuid, EstadoSync.ERROR, ex.message, 1, null, null)
                }
            } catch (ex: java.io.IOException) {
                dao.actualizarEvento(e.uuid, EstadoSync.PENDIENTE, "Sin conexión; se reintentará", 1, null, null)
                return@withContext ResultadoEnvio.Reintentar
            }
        }
        runCatching { refrescar() }
        ResultadoEnvio.Listo
    }

    private fun cuerpoEvento(e: EventoEntity): JsonObject {
        val adj = JSON.decodeFromString(LISTA_ADJ, e.adjuntosJson)
        return buildJsonObject {
            put("uuid", e.uuid)
            put("tipo", e.tipo)
            put("tarea", buildJsonObject { put("tipo", e.tareaTipo); put("id", e.tareaId) })
            put("fecha_dispositivo", e.fechaDispositivo)
            e.lat?.let { put("lat", it) }
            e.lon?.let { put("lon", it) }
            put("app_version", BuildConfig.VERSION_NAME)
            put("datos", JSON.decodeFromString(JsonObject.serializer(), e.datosJson))
            put("adjuntos", buildJsonArray {
                for (a in adj) {
                    val f = File(a.ruta)
                    if (!f.exists()) continue
                    add(buildJsonObject {
                        put("rol", a.rol)
                        put("mimetype", a.mimetype)
                        put("base64", Base64.encodeToString(f.readBytes(), Base64.NO_WRAP))
                    })
                }
            })
        }
    }
}
