package mx.grupo.campo.ui.pantallas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.grupo.campo.data.EstadoSync
import mx.grupo.campo.data.EventoEntity
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.data.SlaCalc
import mx.grupo.campo.data.Tarea
import mx.grupo.campo.data.TareaUi
import mx.grupo.campo.data.Transiciones
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.EstiloMono
import mx.grupo.campo.ui.Rutas
import mx.grupo.campo.ui.Suave
import mx.grupo.campo.ui.componentes.Aviso
import mx.grupo.campo.ui.componentes.BotonGrande
import mx.grupo.campo.ui.componentes.ChipModalidad
import mx.grupo.campo.ui.componentes.ChipSla
import mx.grupo.campo.ui.componentes.ChipSync
import mx.grupo.campo.ui.componentes.Dato
import mx.grupo.campo.ui.componentes.Etiqueta
import mx.grupo.campo.ui.componentes.Seccion
import mx.grupo.campo.ui.componentes.abrirNavegacion
import mx.grupo.campo.ui.componentes.fechaCorta
import mx.grupo.campo.ui.componentes.llamar
import mx.grupo.campo.ui.componentes.nombreModalidad
import java.text.NumberFormat
import java.util.Locale

val NOMBRE_ACCION = mapOf(
    "iniciar_traslado" to "Inició traslado",
    "entregado_cliente" to "Entregado al cliente",
    "entregado_paqueteria" to "Entregado a paquetería",
    "no_entregado" to "No entregado",
    "aceptar" to "Aceptó el servicio",
    "en_camino" to "En camino",
    "en_servicio" to "Inició servicio",
    "pendiente" to "Dejó pendiente",
    "finalizar" to "Finalizó servicio",
    "incidencia" to "Reportó incidencia",
    "materiales_solicitados" to "Solicitó materiales",
    "materiales_utilizados" to "Registró materiales usados",
)

fun numero(d: Double): String = NumberFormat.getNumberInstance(Locale("es", "MX")).apply { maximumFractionDigits = 2 }.format(d)

@Composable
fun DetallePantalla(repo: Repositorio, red: Red, clave: String, nav: NavHostController) {
    val ui by remember(clave) { repo.tareaUi(clave) }.collectAsState(initial = null)
    val ahora = rememberAhora()
    val ctx = LocalContext.current
    val alcance = rememberCoroutineScope()
    var enviando by remember { mutableStateOf(false) }
    LaunchedEffect(clave) { repo.marcarVista(clave) }

    val u = ui
    if (u == null) {
        Marco(repo, red, titulo = "Tarea", atras = { nav.popBackStack() }) {
            Text("Esta tarea ya no está en tu lista.", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(vertical = 24.dp))
        }
        return
    }
    val t = u.tarea
    val cerrada = u.estadoLocal in Transiciones.FINALES || u.retirada

    fun accion(tipo: String) {
        if (enviando) return
        enviando = true
        alcance.launch { repo.registrar(t, tipo); enviando = false }
    }

    val acciones: (@Composable () -> Unit)? = if (cerrada) null else {
        { Acciones(t, u.estadoLocal, nav, enviando, ::accion) }
    }
    Marco(
        repo, red,
        titulo = t.cliente ?: t.referencia,
        subtitulo = listOfNotNull(t.referencia, t.pedido, t.empresa).joinToString(" · "),
        atras = { nav.popBackStack() },
        abrirPendientes = { nav.navigate(Rutas.PENDIENTES) },
        acciones = acciones,
    ) {
        Encabezado(u, ahora)
        if (u.retirada) Aviso("La oficina reasignó o canceló esta tarea. Tus acciones y evidencias se conservan y quedarán registradas en Odoo.", Colores.Vencido)
        if (u.cambioOficina) Aviso("La oficina actualizó datos de esta tarea. Revísalos antes de continuar.", Colores.Directa)

        // Acceso rápido: navegación y llamada, siempre arriba.
        Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            val tel = t.telefono ?: t.direccion?.telefono
            BotonGrande("Navegar", Icons.Filled.Navigation, color = Colores.Tinta, modifier = Modifier.weight(1f).testTag("navegar")) { abrirNavegacion(ctx, t) }
            if (tel != null) BotonGrande("Llamar", Icons.Filled.Call, color = Colores.Tinta, contorno = true, modifier = Modifier.weight(1f)) { llamar(ctx, tel) }
        }

        if (t.esServicio) DatosServicio(t) else DatosEntrega(t)

        if (u.eventos.isNotEmpty()) {
            Seccion("Historial en este teléfono") { LineaTiempo(u.eventos) }
        }
        Seccion("Origen del plazo") {
            Text(t.sla?.fuente?.ifBlank { null } ?: "Sin fecha comprometida en Odoo", style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3)
        }
    }
}

@Composable
private fun Encabezado(u: TareaUi, ahora: java.time.Instant) {
    val t = u.tarea
    val sla = SlaCalc.evaluar(t.sla, u.estadoLocal, u.cumplidoLocal, ahora)
    Column(Modifier.padding(top = 14.dp, bottom = 6.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ChipModalidad(t)
            Spacer(Modifier.width(12.dp))
            if (t.prioridad == "urgente") Etiqueta("URGENTE", Colores.Vencido)
            Spacer(Modifier.weight(1f))
            ChipSync(u.sync, u.pendientes)
        }
        Text(Transiciones.ETIQUETA_ESTADO[u.estadoLocal] ?: u.estadoLocal, style = MaterialTheme.typography.headlineMedium, color = Colores.Tinta,
            modifier = Modifier.testTag("estado"))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(cuentaGrande(sla.limite, ahora, false), style = MaterialTheme.typography.displayLarge.copy(fontSize = 44.sp),
                color = if (sla.limite == null) Colores.Piedra else Colores.deSemaforo(sla.semaforo))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.padding(bottom = 8.dp)) {
                ChipSla(sla)
                fechaCorta(t.sla?.limite)?.let { Text("Límite $it", style = EstiloMono.copy(fontSize = 12.sp, color = Colores.Tinta3)) }
            }
        }
    }
}

@Composable
private fun DatosEntrega(t: Tarea) {
    Seccion("Cliente") {
        Dato("Cliente", t.cliente, destacado = true)
        Dato("Recibe", t.recibe)
        Dato("Teléfono", t.telefono ?: t.direccion?.telefono, mono = true)
        Dato("Referencia del cliente", t.referenciaCliente, mono = true)
    }
    if (t.esPaqueteria) {
        Seccion("Dejar en paquetería") {
            Dato("Destino", t.destinoPaqueteria ?: "Por definir con la oficina", destacado = true)
            Dato("Paquetería indicada", t.paqueteria)
        }
        Seccion("Datos para la guía") {
            val r = t.remitente
            Dato("Remitente", listOfNotNull(r?.nombre, r?.texto?.ifBlank { null }).joinToString("\n").ifBlank { null })
            Dato("Teléfono remitente", r?.telefono, mono = true)
            Dato("Destinatario", listOfNotNull(t.recibe ?: t.cliente, t.direccion?.texto?.ifBlank { null }).joinToString("\n"))
            Dato("Código postal", t.direccion?.cp, mono = true)
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                Dato("Bultos", t.bultos?.toString(), mono = true)
                Dato("Peso", t.pesoKg?.let { "${numero(it)} kg" }, mono = true)
                Dato("Medidas", t.medidas, mono = true)
            }
            if (t.bultos == null && t.pesoKg == null && t.medidas == null) {
                Text("La oficina no capturó bultos, peso ni medidas.", style = MaterialTheme.typography.bodyMedium, color = Colores.PorVencer)
            }
        }
    } else {
        Seccion("Dirección de entrega") {
            Dato("Dirección", t.direccion?.texto, destacado = true)
            Dato("Referencias", t.direccion?.referencias)
        }
    }
    Seccion("Ventana") {
        val v = listOfNotNull(fechaCorta(t.ventanaInicio), fechaCorta(t.ventanaFin)).joinToString(" → ")
        Dato("Horario", v.ifBlank { "Sin ventana definida" }, mono = true)
    }
    if (!t.instrucciones.isNullOrBlank()) Seccion("Instrucciones") { Text(t.instrucciones, style = MaterialTheme.typography.bodyLarge) }
    Seccion("Productos (${t.productos.size})") { TablaProductos(t) }
    if (t.guia != null || t.motivoNoEntrega != null) {
        Seccion("Registrado en Odoo") {
            Dato("Paquetería", t.paqueteria)
            Dato("Guía", t.guia, mono = true)
            Dato("Precio", t.costoEnvio?.let { "${numero(it)} ${t.moneda ?: ""}" }, mono = true)
            Dato("Motivo de no entrega", t.motivoNoEntrega)
        }
    }
}

@Composable
private fun TablaProductos(t: Tarea) {
    Column {
        t.productos.forEachIndexed { i, p ->
            if (i > 0) HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
            Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
                Text("${numero(p.cantidad)} ${p.unidad ?: ""}".trim(), style = EstiloMono.copy(fontWeight = FontWeight.Bold, color = Colores.Tinta),
                    modifier = Modifier.width(88.dp))
                Text(p.producto ?: p.descripcion ?: "—", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DatosServicio(t: Tarea) {
    Seccion("Servicio") {
        Dato("Tipo", nombreModalidad(t).lowercase().replaceFirstChar { it.uppercase() }, destacado = true)
        Dato("Etapa en Odoo", t.etapa)
        Dato("Equipo", t.equipo)
        Dato("Descripción", t.descripcion)
    }
    Seccion("Cliente") {
        Dato("Cliente", t.cliente, destacado = true)
        Dato("Teléfono", t.telefono ?: t.direccion?.telefono, mono = true)
        Dato("Dirección", t.direccion?.texto)
        Dato("Referencias", t.direccion?.referencias)
    }
    Seccion("Ventana") {
        val v = listOfNotNull(fechaCorta(t.ventanaInicio), fechaCorta(t.ventanaFin)).joinToString(" → ")
        Dato("Horario", v.ifBlank { "Sin ventana definida" }, mono = true)
    }
    if (!t.instrucciones.isNullOrBlank()) Seccion("Instrucciones") { Text(t.instrucciones, style = MaterialTheme.typography.bodyLarge) }
    val sol = t.materiales.filter { it.tipo == "solicitado" }
    val uso = t.materiales.filter { it.tipo == "utilizado" }
    Seccion("Materiales") {
        Row(Modifier.fillMaxWidth()) {
            Column(Modifier.weight(1f)) {
                Text("SOLICITADOS", style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
                if (sol.isEmpty()) Text("—", style = MaterialTheme.typography.bodyMedium, color = Colores.Piedra)
                sol.forEach { m ->
                    Text("${numero(m.cantidad)} · ${m.descripcion}", style = MaterialTheme.typography.bodyMedium)
                    m.estado?.let { Text(it.uppercase(), style = MaterialTheme.typography.labelMedium, color = if (it == "autorizado" || it == "entregado") Colores.ATiempo else Colores.PorVencer) }
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text("UTILIZADOS", style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
                if (uso.isEmpty()) Text("—", style = MaterialTheme.typography.bodyMedium, color = Colores.Piedra)
                uso.forEach { m -> Text("${numero(m.cantidad)} · ${m.descripcion}", style = MaterialTheme.typography.bodyMedium) }
            }
        }
    }
    if (!t.motivoPendiente.isNullOrBlank()) Seccion("Pendiente por") { Text(t.motivoPendiente, style = MaterialTheme.typography.bodyLarge) }
}

/** Línea de tiempo que se dibuja con escalonado (firma de la dirección de diseño). */
@Composable
private fun LineaTiempo(eventos: List<EventoEntity>) {
    Column {
        eventos.forEachIndexed { i, e ->
            val p = remember(e.uuid) { Animatable(0f) }
            LaunchedEffect(e.uuid) { delay(60L * i); p.animateTo(1f, tween(240, easing = Suave)) }
            val color = when (e.estado) {
                EstadoSync.SINCRONIZADO -> Colores.ATiempo
                EstadoSync.ERROR, EstadoSync.RECHAZADO -> Colores.Vencido
                else -> Colores.PorVencer
            }
            Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min).graphicsLayer { alpha = p.value }) {
                Box(Modifier.width(18.dp).fillMaxHeight()) {
                    Canvas(Modifier.width(18.dp).fillMaxHeight()) {
                        val x = size.width / 2
                        if (i < eventos.lastIndex) drawLine(Colores.Linea, Offset(x, 14f), Offset(x, 14f + (size.height - 14f) * p.value), strokeWidth = 2f)
                        drawCircle(color, radius = 5.dp.toPx() * p.value, center = Offset(x, 14f))
                    }
                }
                Column(Modifier.padding(start = 8.dp, bottom = 14.dp)) {
                    Text(NOMBRE_ACCION[e.tipo] ?: e.tipo, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Text(fechaCorta(e.fechaDispositivo) ?: e.fechaDispositivo, style = EstiloMono.copy(fontSize = 12.sp, color = Colores.Tinta3))
                    val txt = when (e.estado) {
                        EstadoSync.SINCRONIZADO -> "Confirmado por Odoo"
                        EstadoSync.ENVIANDO -> "Enviando…"
                        EstadoSync.PENDIENTE -> e.mensaje ?: "Guardado en el teléfono, pendiente de enviar"
                        EstadoSync.ERROR -> "Error: ${e.mensaje ?: "sin detalle"}"
                        EstadoSync.RECHAZADO -> "Rechazado: ${e.mensaje ?: ""}"
                        else -> e.estado
                    }
                    Text(txt, style = MaterialTheme.typography.bodyMedium, color = color)
                }
            }
        }
    }
}

@Composable
private fun Acciones(t: Tarea, estado: String, nav: NavHostController, enviando: Boolean, accion: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        if (!t.esServicio) {
            val color = Colores.deTarea(t)
            if (estado == "asignado" || estado == "no_entregado") {
                BotonGrande("Iniciar traslado", Icons.Filled.DirectionsCar, color = Colores.Tinta, habilitado = !enviando,
                    modifier = Modifier.testTag("iniciar_traslado")) { accion("iniciar_traslado") }
            }
            if (t.esPaqueteria) {
                BotonGrande("Entregué a paquetería", Icons.Filled.Store, color = color, modifier = Modifier.testTag("confirmar")) { nav.navigate(Rutas.paqueteria(t.clave)) }
            } else {
                BotonGrande("Confirmar entrega", Icons.Filled.CheckCircle, color = color, modifier = Modifier.testTag("confirmar")) { nav.navigate(Rutas.entrega(t.clave)) }
            }
            BotonGrande("No se pudo entregar", Icons.Filled.ReportProblem, color = Colores.Vencido, contorno = true) {
                nav.navigate(Rutas.motivo(t.clave, "no_entregado"))
            }
        } else {
            when (estado) {
                "asignado" -> BotonGrande("Voy en camino", Icons.Filled.LocalShipping, color = Colores.Tinta, habilitado = !enviando,
                    modifier = Modifier.testTag("en_camino")) { accion("en_camino") }
                "en_camino", "pendiente" -> BotonGrande(if (estado == "pendiente") "Reanudar servicio" else "Llegué · iniciar servicio",
                    Icons.Filled.PlayArrow, color = Colores.Tinta, habilitado = !enviando, modifier = Modifier.testTag("en_servicio")) { accion("en_servicio") }
                "en_servicio" -> {
                    BotonGrande("Finalizar servicio", Icons.Filled.CheckCircle, color = Colores.Servicio, modifier = Modifier.testTag("finalizar")) {
                        nav.navigate(Rutas.finalizar(t.clave))
                    }
                    BotonGrande("Dejar pendiente", Icons.Filled.PauseCircle, color = Colores.Tinta, contorno = true) { nav.navigate(Rutas.motivo(t.clave, "pendiente")) }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                BotonGrande("Materiales", Icons.Filled.Inventory2, color = Colores.Tinta, contorno = true, modifier = Modifier.weight(1f)) {
                    nav.navigate(Rutas.materiales(t.clave))
                }
                BotonGrande("Incidencia", Icons.Filled.Build, color = Colores.Vencido, contorno = true, modifier = Modifier.weight(1f)) {
                    nav.navigate(Rutas.motivo(t.clave, "incidencia"))
                }
            }
        }
    }
}
