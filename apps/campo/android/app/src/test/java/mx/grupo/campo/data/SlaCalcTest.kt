package mx.grupo.campo.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class SlaCalcTest {
    private val ahora = Instant.parse("2026-11-10T18:00:00Z")
    private fun sla(limite: String?, pausado: Boolean = false, cumplido: String? = null) =
        SlaInfo(limite = limite, fuente = "Fecha límite de la entrega", pausado = pausado, cumplidoEn = cumplido)

    @Test fun sinFechaNoInventaPlazo() {
        val e = SlaCalc.evaluar(sla(null), "asignado", null, ahora)
        assertEquals(Semaforo.SIN_FECHA, e.semaforo)
        assertEquals(null, e.limite)
    }

    @Test fun verdeConTiempo() {
        val e = SlaCalc.evaluar(sla("2026-11-11T18:00:00Z"), "asignado", null, ahora)
        assertEquals(Semaforo.VERDE, e.semaforo)
        assertEquals("Faltan 1 d 0 h", e.texto)
    }

    @Test fun ambarCercaDelLimite() {
        val e = SlaCalc.evaluar(sla("2026-11-10T19:30:00Z"), "en_traslado", null, ahora)
        assertEquals(Semaforo.AMBAR, e.semaforo)
        assertEquals("Vence en 1 h 30 min", e.texto)
    }

    @Test fun rojoConRetraso() {
        val e = SlaCalc.evaluar(sla("2026-11-10T17:15:00Z"), "asignado", null, ahora)
        assertEquals(Semaforo.ROJO, e.semaforo)
        assertEquals("Retraso de 45 min", e.texto)
    }

    @Test fun cumplidoATiempoYTarde() {
        val limite = "2026-11-10T17:00:00Z"
        assertEquals(Semaforo.CUMPLIDO_A_TIEMPO,
            SlaCalc.evaluar(sla(limite), "entregado_cliente", Instant.parse("2026-11-10T16:59:00Z"), ahora).semaforo)
        val tarde = SlaCalc.evaluar(sla(limite), "finalizado", Instant.parse("2026-11-10T17:20:00Z"), ahora)
        assertEquals(Semaforo.CUMPLIDO_TARDE, tarde.semaforo)
        assertTrue(tarde.texto.contains("20 min"))
    }

    @Test fun pendienteQuedaEnPausa() {
        assertEquals(Semaforo.PAUSADO, SlaCalc.evaluar(sla("2026-11-10T17:00:00Z"), "pendiente", null, ahora).semaforo)
        assertEquals(Semaforo.PAUSADO, SlaCalc.evaluar(sla("2026-11-10T17:00:00Z", pausado = true), "en_servicio", null, ahora).semaforo)
    }

    @Test fun ordenVencidasPrimeroYCerradasAlFinal() {
        fun t(id: Int, limite: String?, prioridad: String = "normal") = Tarea(tipo = "entrega", id = id, prioridad = prioridad, sla = sla(limite))
        val vencida = t(1, "2026-11-10T10:00:00Z")
        val urgente = t(2, "2026-11-12T10:00:00Z", "urgente")
        val normal = t(3, "2026-11-11T10:00:00Z")
        val sinFecha = t(4, null)
        val orden = listOf(sinFecha, normal, urgente, vencida).sortedBy { SlaCalc.prioridadOrden(it, "asignado", ahora) }.map { it.id }
        assertEquals(listOf(1, 2, 3, 4), orden)
        assertEquals(Long.MAX_VALUE, SlaCalc.prioridadOrden(normal, "entregado_cliente", ahora))
    }

    @Test fun tareaSeLeeDelJsonDelPuente() {
        val json = """{"tipo":"entrega","id":9001,"referencia":"PRUEB/OUT/00001","modalidad":"paqueteria","peso_kg":12.5,
            "sla":{"limite":"2026-11-11T18:00:00Z","fuente":"Fecha límite","pausado":false},"campo_nuevo":1}"""
        val t = JSON.decodeFromString(Tarea.serializer(), json)
        assertTrue(t.esPaqueteria)
        assertEquals(12.5, t.pesoKg!!, 0.0)
        assertEquals("entrega:9001", t.clave)
    }
}
