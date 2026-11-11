package mx.grupo.campo.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/** Copia local de una tarea (JSON del puente) para trabajar sin señal. */
@Entity(tableName = "tareas")
data class TareaEntity(
    @PrimaryKey val clave: String, // "entrega:9001"
    val operadorId: Int,
    val json: String,
    val huella: String, // datos que, si cambian desde Odoo, se avisan como "Actualizado por oficina"
    val cambioOficina: Boolean = false,
    val retirada: Boolean = false, // ya no viene del servidor (reasignada o cancelada) pero tiene acciones sin enviar
    val recibidaMs: Long = System.currentTimeMillis(),
)

/** Una acción capturada en campo. No se borra hasta que Odoo confirma. */
@Entity(tableName = "eventos")
data class EventoEntity(
    @PrimaryKey val uuid: String,
    val operadorId: Int,
    val tareaClave: String,
    val tareaTipo: String,
    val tareaId: Int,
    val tipo: String,
    val datosJson: String,
    val adjuntosJson: String, // lista de AdjuntoLocal
    val fechaDispositivo: String,
    val lat: Double?,
    val lon: Double?,
    val estado: String = EstadoSync.PENDIENTE,
    val mensaje: String? = null,
    val intentos: Int = 0,
    val creadoMs: Long = System.currentTimeMillis(),
    val sincronizadoMs: Long? = null,
    val eventoIdOdoo: Int? = null,
)

@kotlinx.serialization.Serializable
data class AdjuntoLocal(val rol: String, val ruta: String, val mimetype: String)

@Dao
interface CampoDao {
    @Query("SELECT * FROM tareas WHERE operadorId = :op")
    fun tareas(op: Int): Flow<List<TareaEntity>>

    @Query("SELECT * FROM tareas WHERE operadorId = :op")
    suspend fun tareasAhora(op: Int): List<TareaEntity>

    @Query("SELECT * FROM tareas WHERE clave = :clave")
    fun tarea(clave: String): Flow<TareaEntity?>

    @Upsert
    suspend fun guardarTareas(t: List<TareaEntity>)

    @Query("DELETE FROM tareas WHERE clave IN (:claves)")
    suspend fun borrarTareas(claves: List<String>)

    @Query("UPDATE tareas SET cambioOficina = 0 WHERE clave = :clave")
    suspend fun marcarVista(clave: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertarEvento(e: EventoEntity)

    @Query("SELECT * FROM eventos WHERE operadorId = :op ORDER BY creadoMs DESC")
    fun eventos(op: Int): Flow<List<EventoEntity>>

    @Query("SELECT * FROM eventos WHERE tareaClave = :clave ORDER BY creadoMs ASC")
    fun eventosDeTarea(clave: String): Flow<List<EventoEntity>>

    @Query("SELECT * FROM eventos WHERE operadorId = :op AND estado IN ('PENDIENTE','ENVIANDO') ORDER BY creadoMs ASC")
    suspend fun porEnviar(op: Int): List<EventoEntity>

    @Query("SELECT COUNT(*) FROM eventos WHERE operadorId != :op AND estado IN ('PENDIENTE','ENVIANDO','ERROR')")
    suspend fun pendientesDeOtros(op: Int): Int

    @Query("SELECT DISTINCT operadorId FROM eventos WHERE estado IN ('PENDIENTE','ENVIANDO','ERROR')")
    suspend fun operadoresConPendientes(): List<Int>

    @Query("SELECT COUNT(*) FROM eventos WHERE tareaClave = :clave AND estado IN ('PENDIENTE','ENVIANDO','ERROR')")
    suspend fun abiertosDeTarea(clave: String): Int

    @Query("UPDATE eventos SET estado = :estado, mensaje = :mensaje, intentos = intentos + :sumar, sincronizadoMs = :sinc, eventoIdOdoo = :idOdoo WHERE uuid = :uuid")
    suspend fun actualizarEvento(uuid: String, estado: String, mensaje: String?, sumar: Int, sinc: Long?, idOdoo: Int?)

    @Query("UPDATE eventos SET estado = 'PENDIENTE', mensaje = NULL WHERE uuid = :uuid AND estado = 'ERROR'")
    suspend fun reintentar(uuid: String)

    @Query("DELETE FROM eventos WHERE uuid = :uuid AND estado = 'ERROR'")
    suspend fun descartar(uuid: String)

    @Query("DELETE FROM eventos WHERE estado IN ('SINCRONIZADO','RECHAZADO') AND sincronizadoMs < :antesDe")
    suspend fun limpiarViejos(antesDe: Long)

    @Transaction
    suspend fun reemplazarTareas(op: Int, nuevas: List<TareaEntity>, retirarConPendientes: List<String>, borrar: List<String>) {
        if (borrar.isNotEmpty()) borrarTareas(borrar)
        guardarTareas(nuevas)
        if (retirarConPendientes.isNotEmpty()) marcarRetiradas(retirarConPendientes)
    }

    @Query("UPDATE tareas SET retirada = 1 WHERE clave IN (:claves)")
    suspend fun marcarRetiradas(claves: List<String>)
}

@Database(entities = [TareaEntity::class, EventoEntity::class], version = 1, exportSchema = true)
abstract class CampoDb : RoomDatabase() {
    abstract fun dao(): CampoDao

    companion object {
        @Volatile private var instancia: CampoDb? = null
        fun de(ctx: Context): CampoDb = instancia ?: synchronized(this) {
            instancia ?: Room.databaseBuilder(ctx.applicationContext, CampoDb::class.java, "campo.db").build().also { instancia = it }
        }
    }
}
