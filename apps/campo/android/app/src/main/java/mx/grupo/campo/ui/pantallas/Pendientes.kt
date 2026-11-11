package mx.grupo.campo.ui.pantallas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import mx.grupo.campo.data.EstadoSync
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.EstiloMono
import mx.grupo.campo.ui.componentes.Etiqueta
import mx.grupo.campo.ui.componentes.fechaCorta

/** Bitácora de acciones de esta persona en el teléfono, con su estado de envío a Odoo. */
@Composable
fun PendientesPantalla(repo: Repositorio, red: Red, atras: () -> Unit, abrir: (String) -> Unit) {
    val eventos by remember(repo) { repo.eventosUi() }.collectAsState(initial = emptyList())
    val alcance = rememberCoroutineScope()
    val porEnviar = eventos.count { it.estado == EstadoSync.PENDIENTE || it.estado == EstadoSync.ENVIANDO }
    val errores = eventos.count { it.estado == EstadoSync.ERROR }

    Marco(repo, red, titulo = "Envíos a Odoo", subtitulo = "POR ENVIAR %02d · CON ERROR %02d".format(porEnviar, errores), atras = atras) {
        if (eventos.isEmpty()) {
            Text("Aún no hay acciones registradas en este teléfono.", style = MaterialTheme.typography.bodyLarge,
                color = Colores.Tinta3, modifier = Modifier.padding(vertical = 24.dp))
        }
        Text("Una acción solo aparece como «Confirmado por Odoo» cuando Odoo respondió que la guardó. Las fotos y firmas se quedan en el teléfono hasta entonces.",
            style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3, modifier = Modifier.padding(vertical = 12.dp))
        eventos.forEach { e ->
            HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
            Column(Modifier.fillMaxWidth().clickable { abrir(e.tareaClave) }.padding(vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(NOMBRE_ACCION[e.tipo] ?: e.tipo, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    val (txt, color) = when (e.estado) {
                        EstadoSync.SINCRONIZADO -> "Sincronizado" to Colores.ATiempo
                        EstadoSync.ENVIANDO -> "Enviando" to Colores.PorVencer
                        EstadoSync.PENDIENTE -> "Pendiente" to Colores.PorVencer
                        EstadoSync.RECHAZADO -> "Rechazado por oficina" to Colores.Vencido
                        else -> "Error" to Colores.Vencido
                    }
                    Etiqueta(txt, color)
                }
                Text("${e.tareaTipo.uppercase()} #${e.tareaId} · ${fechaCorta(e.fechaDispositivo) ?: ""}", style = EstiloMono.copy(fontSize = 12.sp, color = Colores.Tinta3))
                Text("Folio ${e.uuid.take(8)} · intentos ${e.intentos}", style = EstiloMono.copy(fontSize = 12.sp, color = Colores.Piedra))
                e.mensaje?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = if (e.estado == EstadoSync.ERROR || e.estado == EstadoSync.RECHAZADO) Colores.Vencido else Colores.Tinta3) }
                if (e.estado == EstadoSync.ERROR) {
                    Row {
                        TextButton(onClick = { alcance.launch { repo.reintentar(e.uuid) } }) { Text("Reintentar", color = Colores.Tinta) }
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = { alcance.launch { repo.descartar(e) } }) { Text("Descartar", color = Colores.Vencido) }
                    }
                }
            }
        }
    }
}
