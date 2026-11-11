package mx.grupo.campo.ui.pantallas

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.data.SlaCalc
import mx.grupo.campo.data.TareaUi
import mx.grupo.campo.data.Transiciones
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.EstiloMono
import mx.grupo.campo.ui.Suave
import mx.grupo.campo.ui.componentes.BotonGrande
import mx.grupo.campo.ui.componentes.ChipModalidad
import mx.grupo.campo.ui.componentes.ChipSla
import mx.grupo.campo.ui.componentes.ChipSync
import mx.grupo.campo.ui.componentes.Etiqueta
import mx.grupo.campo.ui.componentes.nombreModalidad
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** Hora actual que se actualiza cada 30 s para que las cuentas regresivas avancen solas. */
@Composable
fun rememberAhora(): Instant {
    val ahora by produceState(Instant.now()) {
        while (true) { delay(30_000); value = Instant.now() }
    }
    return ahora
}

private enum class Filtro(val texto: String) { ACTIVAS("Activas"), CERRADAS("Cerradas"), TODAS("Todas") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasPantalla(repo: Repositorio, red: Red, abrir: (String) -> Unit, abrirPendientes: () -> Unit) {
    val lista by remember(repo) { repo.tareasUi() }.collectAsState(initial = null)
    val operador by repo.sesion.operador.collectAsState()
    val expirada by repo.sesion.expirada.collectAsState()
    val ahora = rememberAhora()
    var filtro by rememberSaveable { mutableStateOf(Filtro.ACTIVAS) }
    var refrescando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var salir by remember { mutableStateOf(false) }
    val alcance = rememberCoroutineScope()

    fun refrescar() {
        if (refrescando) return
        refrescando = true
        alcance.launch {
            error = try { repo.refrescar(); null } catch (e: Exception) { mensajeDeError(e) }
            refrescando = false
        }
    }
    LaunchedEffect(Unit) {
        while (true) { refrescar(); delay(5 * 60_000) }
    }

    val esTecnico = operador?.rol == "tecnico"
    val hoy = LocalDate.now().format(DateTimeFormatter.ofPattern("EEE d MMM", Locale("es", "MX"))).uppercase()

    Marco(
        repo, red,
        titulo = operador?.nombre ?: "Mis tareas",
        subtitulo = "${if (esTecnico) "TÉCNICO" else "TRANSPORTISTA"} · $hoy",
        abrirPendientes = abrirPendientes,
        desplazable = false,
    ) {
        if (expirada) {
            Column(Modifier.padding(16.dp)) {
                mx.grupo.campo.ui.componentes.Aviso("Tu sesión venció. Tus acciones siguen guardadas; vuelve a entrar para enviarlas.", Colores.Vencido)
                Spacer(Modifier.height(8.dp))
                BotonGrande("Volver a iniciar sesión", Icons.AutoMirrored.Filled.Logout) { repo.sesion.cerrar() }
            }
        }
        val todas = lista
        val activas = todas?.filter { it.estadoLocal !in Transiciones.FINALES && !it.retirada } ?: emptyList()
        val visibles = when (filtro) {
            Filtro.ACTIVAS -> activas + (todas?.filter { it.retirada } ?: emptyList())
            Filtro.CERRADAS -> todas?.filter { it.estadoLocal in Transiciones.FINALES } ?: emptyList()
            Filtro.TODAS -> todas ?: emptyList()
        }

        // Filtros: texto mono, subrayado del activo. Conteos tabulares.
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Filtro.entries.forEach { f ->
                val n = when (f) {
                    Filtro.ACTIVAS -> activas.size
                    Filtro.CERRADAS -> todas?.count { it.estadoLocal in Transiciones.FINALES } ?: 0
                    Filtro.TODAS -> todas?.size ?: 0
                }
                val activo = f == filtro
                Column(Modifier.clickable { filtro = f }.padding(end = 20.dp, top = 6.dp, bottom = 6.dp).testTag("filtro_${f.name}")) {
                    Text("${f.texto.uppercase()} ${"%02d".format(n)}", style = MaterialTheme.typography.labelMedium,
                        color = if (activo) Colores.Tinta else Colores.Piedra, fontWeight = if (activo) FontWeight.Bold else FontWeight.Medium)
                    Spacer(Modifier.height(4.dp))
                    Box(Modifier.height(2.dp).width(28.dp).background(if (activo) Colores.Tinta else Colores.Fondo))
                }
            }
            Spacer(Modifier.weight(1f))
            IconButton(onClick = { refrescar() }) { Icon(Icons.Filled.Refresh, "Actualizar", tint = Colores.Tinta3) }
            IconButton(onClick = { salir = true }) { Icon(Icons.AutoMirrored.Filled.Logout, "Cerrar sesión", tint = Colores.Tinta3) }
        }
        HorizontalDivider(thickness = 1.dp, color = Colores.Linea)

        PullToRefreshBox(isRefreshing = refrescando, onRefresh = { refrescar() }, modifier = Modifier.fillMaxSize()) {
            when {
                todas == null -> Esqueleto()
                visibles.isEmpty() -> Vacio(filtro, error, esTecnico)
                else -> LazyColumn(Modifier.fillMaxSize().testTag("lista_tareas")) {
                    if (error != null) item {
                        Text("No se pudo actualizar: $error. Se muestran los datos guardados.",
                            style = MaterialTheme.typography.bodyMedium, color = Colores.Vencido, modifier = Modifier.padding(16.dp))
                    }
                    val siguiente = if (filtro != Filtro.CERRADAS) visibles.firstOrNull { it.estadoLocal !in Transiciones.FINALES && !it.retirada } else null
                    if (siguiente != null) item(key = "siguiente") { Siguiente(siguiente, ahora) { abrir(siguiente.tarea.clave) } }
                    itemsIndexed(visibles.filter { it !== siguiente }, key = { _, t -> t.tarea.clave }) { i, t ->
                        Fila(t, ahora, i) { abrir(t.tarea.clave) }
                    }
                    item { Spacer(Modifier.navigationBarsPadding().height(32.dp)) }
                }
            }
        }
    }

    if (salir) {
        AlertDialog(
            onDismissRequest = { salir = false },
            title = { Text("¿Cerrar sesión?") },
            text = { Text("Las acciones que no se hayan enviado se quedan guardadas en el teléfono y se envían cuando vuelvas a entrar.") },
            confirmButton = { TextButton(onClick = { salir = false; repo.sesion.cerrar() }) { Text("Cerrar sesión", color = Colores.Vencido) } },
            dismissButton = { TextButton(onClick = { salir = false }) { Text("Cancelar", color = Colores.Tinta) } },
        )
    }
}

/** La firma de la dirección: la próxima tarea con su cuenta regresiva grande en mono tabular. */
@Composable
private fun Siguiente(t: TareaUi, ahora: Instant, onClick: () -> Unit) {
    val sla = SlaCalc.evaluar(t.tarea.sla, t.estadoLocal, t.cumplidoLocal, ahora)
    val color = Colores.deSemaforo(sla.semaforo)
    Column(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp).testTag("siguiente")) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("SIGUIENTE", style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
            Spacer(Modifier.width(10.dp))
            ChipModalidad(t.tarea)
            Spacer(Modifier.weight(1f))
            Text(t.tarea.referencia, style = EstiloMono.copy(color = Colores.Tinta2, fontSize = 13.sp))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            cuentaGrande(sla.limite, ahora, t.tarea.sla?.limite == null),
            style = MaterialTheme.typography.displayLarge, color = if (sla.limite == null) Colores.Piedra else color,
            modifier = Modifier.testTag("cuenta_regresiva"),
        )
        ChipSla(sla)
        Spacer(Modifier.height(10.dp))
        Text(t.tarea.cliente ?: "—", style = MaterialTheme.typography.titleMedium, color = Colores.Tinta, maxLines = 1, overflow = TextOverflow.Ellipsis)
        Text(lineaLugar(t), style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Etiqueta(Transiciones.ETIQUETA_ESTADO[t.estadoLocal] ?: t.estadoLocal, Colores.Tinta3)
            Spacer(Modifier.width(14.dp))
            ChipSync(t.sync, t.pendientes)
        }
    }
    HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
}

/** "−01:25" si va tarde, "03:40" si faltan horas, "2d 04h" si faltan días. */
fun cuentaGrande(limite: Instant?, ahora: Instant, sinFecha: Boolean): String {
    if (limite == null || sinFecha) return "--:--"
    val seg = java.time.Duration.between(ahora, limite).seconds
    val signo = if (seg < 0) "−" else ""
    val abs = kotlin.math.abs(seg) / 60
    val dias = abs / (60 * 24)
    val h = (abs / 60) % 24
    val m = abs % 60
    return if (dias > 0) "$signo${dias}d ${"%02d".format(h)}h" else "$signo${"%02d".format(h)}:${"%02d".format(m)}"
}

private fun lineaLugar(t: TareaUi): String {
    val x = t.tarea
    return when {
        x.esPaqueteria -> "Dejar en: " + (x.destinoPaqueteria ?: x.paqueteria ?: "paquetería por definir")
        else -> listOfNotNull(x.direccion?.calle, x.direccion?.colonia, x.direccion?.ciudad).joinToString(", ").ifBlank { x.direccion?.texto ?: "" }
    }
}

@Composable
private fun Fila(t: TareaUi, ahora: Instant, indice: Int, onClick: () -> Unit) {
    // Entrada escalonada: 50 ms por fila, solo las primeras 12 para no retrasar listas largas.
    val alfa = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(50L * minOf(indice, 12))
        alfa.animateTo(1f, tween(220, easing = Suave))
    }
    val sla = SlaCalc.evaluar(t.tarea.sla, t.estadoLocal, t.cumplidoLocal, ahora)
    val cerrada = t.estadoLocal in Transiciones.FINALES
    Column(Modifier.graphicsLayer { alpha = alfa.value; translationY = (1f - alfa.value) * 24f }) {
        Row(
            Modifier.fillMaxWidth().height(IntrinsicSize.Min).clickable(onClick = onClick).padding(vertical = 12.dp).padding(end = 8.dp)
                .testTag("fila_${t.tarea.clave}"),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Barra de tipo: único lugar donde el color de modalidad ocupa área.
            Box(Modifier.width(4.dp).fillMaxHeight().clip(RoundedCornerShape(topEnd = 2.dp, bottomEnd = 2.dp)).background(Colores.deTarea(t.tarea)))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(t.tarea.referencia, style = EstiloMono.copy(fontSize = 13.sp, color = Colores.Tinta2))
                    Spacer(Modifier.width(8.dp))
                    Text(nombreModalidad(t.tarea), style = MaterialTheme.typography.labelMedium, color = Colores.deTarea(t.tarea))
                    if (t.tarea.prioridad == "urgente") {
                        Spacer(Modifier.width(8.dp)); Text("URGENTE", style = MaterialTheme.typography.labelMedium, color = Colores.Vencido)
                    }
                }
                Text(t.tarea.cliente ?: "—", style = MaterialTheme.typography.titleMedium,
                    color = if (cerrada) Colores.Tinta3 else Colores.Tinta, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(lineaLugar(t), style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Etiqueta(Transiciones.ETIQUETA_ESTADO[t.estadoLocal] ?: t.estadoLocal, Colores.Tinta3)
                    ChipSync(t.sync, t.pendientes)
                }
                when {
                    t.retirada -> Text("Retirada por la oficina · tus acciones se conservan", style = MaterialTheme.typography.bodyMedium, color = Colores.Vencido)
                    t.cambioOficina -> Text("Actualizada por la oficina", style = MaterialTheme.typography.bodyMedium, color = Colores.Directa)
                }
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                Text(cuentaGrande(sla.limite, ahora, false), style = EstiloMono.copy(fontSize = 17.sp, fontWeight = FontWeight.Bold,
                    color = if (sla.limite == null || cerrada) Colores.Piedra else Colores.deSemaforo(sla.semaforo)))
                Spacer(Modifier.height(2.dp))
                Box(Modifier.width(8.dp).height(8.dp).clip(RoundedCornerShape(50)).background(Colores.deSemaforo(sla.semaforo)))
            }
            Icon(Icons.Filled.ChevronRight, null, tint = Colores.Piedra)
        }
        HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
    }
}

@Composable
private fun Esqueleto() {
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        repeat(5) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Box(Modifier.width(110.dp).height(12.dp).clip(RoundedCornerShape(4.dp)).background(Colores.Linea))
                Box(Modifier.fillMaxWidth(0.7f).height(16.dp).clip(RoundedCornerShape(4.dp)).background(Colores.Linea))
                Box(Modifier.fillMaxWidth(0.9f).height(12.dp).clip(RoundedCornerShape(4.dp)).background(Colores.Linea))
            }
        }
    }
}

@Composable
private fun Vacio(filtro: Filtro, error: String?, esTecnico: Boolean) {
    LazyColumn(Modifier.fillMaxSize()) {
        item {
            Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("00", style = MaterialTheme.typography.displayLarge, color = Colores.Piedra)
                Text(
                    when (filtro) {
                        Filtro.ACTIVAS -> if (esTecnico) "No tienes servicios asignados" else "No tienes entregas asignadas"
                        Filtro.CERRADAS -> "Aún no cierras tareas en los últimos 2 días"
                        Filtro.TODAS -> "Sin tareas"
                    },
                    style = MaterialTheme.typography.titleMedium, color = Colores.Tinta,
                )
                Text(
                    error?.let { "No se pudo consultar a la oficina: $it" }
                        ?: "Cuando la oficina te asigne algo en Odoo aparecerá aquí. Desliza hacia abajo para actualizar.",
                    style = MaterialTheme.typography.bodyMedium, color = if (error != null) Colores.Vencido else Colores.Tinta3,
                )
            }
        }
    }
}
