package mx.grupo.campo.data

import java.time.Duration
import java.time.Instant

/** Semáforo de cumplimiento. Los tiempos vienen de Odoo; aquí solo se compara contra la hora actual. */
enum class Semaforo { VERDE, AMBAR, ROJO, SIN_FECHA, CUMPLIDO_A_TIEMPO, CUMPLIDO_TARDE, PAUSADO }

data class EstadoSla(val semaforo: Semaforo, val texto: String, val limite: Instant?)

object SlaCalc {
    /** Minutos antes del límite en que se considera "por vencer". Valor visual; la regla de negocio está pendiente. */
    var umbralAmbarMin: Long = 120

    fun parse(iso: String?): Instant? = try {
        if (iso.isNullOrBlank()) null else Instant.parse(iso)
    } catch (_: Exception) {
        null
    }

    fun duracion(d: Duration): String {
        val totalMin = d.abs().toMinutes()
        val dias = totalMin / (60 * 24)
        val horas = (totalMin / 60) % 24
        val min = totalMin % 60
        return when {
            dias > 0 -> "$dias d ${horas} h"
            horas > 0 -> "$horas h ${min} min"
            else -> "$min min"
        }
    }

    fun evaluar(sla: SlaInfo?, estadoLocal: String, cumplidoLocal: Instant? = null, ahora: Instant = Instant.now()): EstadoSla {
        val limite = parse(sla?.limite)
        val cumplido = cumplidoLocal ?: parse(sla?.cumplidoEn)
        if (limite == null) return EstadoSla(Semaforo.SIN_FECHA, sla?.fuente?.ifBlank { null } ?: "Sin fecha comprometida", null)
        if (estadoLocal in Transiciones.FINALES && cumplido != null) {
            return if (!cumplido.isAfter(limite)) EstadoSla(Semaforo.CUMPLIDO_A_TIEMPO, "Cumplido a tiempo", limite)
            else EstadoSla(Semaforo.CUMPLIDO_TARDE, "Cumplido con ${duracion(Duration.between(limite, cumplido))} de retraso", limite)
        }
        if (sla?.pausado == true || estadoLocal == "pendiente") {
            return EstadoSla(Semaforo.PAUSADO, "En pausa (pendiente)", limite)
        }
        val restante = Duration.between(ahora, limite)
        return when {
            restante.isNegative -> EstadoSla(Semaforo.ROJO, "Retraso de ${duracion(restante)}", limite)
            restante.toMinutes() <= umbralAmbarMin -> EstadoSla(Semaforo.AMBAR, "Vence en ${duracion(restante)}", limite)
            else -> EstadoSla(Semaforo.VERDE, "Faltan ${duracion(restante)}", limite)
        }
    }

    /** Orden de atención: vencidas primero, luego urgentes, luego por fecha límite. */
    fun prioridadOrden(t: Tarea, estadoLocal: String, ahora: Instant = Instant.now()): Long {
        if (estadoLocal in Transiciones.FINALES) return Long.MAX_VALUE
        val limite = parse(t.sla?.limite)?.epochSecond ?: (Long.MAX_VALUE / 4)
        val urgente = if (t.prioridad == "urgente") -1_000_000_000L else 0L
        val vencida = if (limite < ahora.epochSecond) -2_000_000_000L else 0L
        return limite + urgente + vencida
    }
}
