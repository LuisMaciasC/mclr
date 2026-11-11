package mx.grupo.campo.ui.pantallas

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.map
import mx.grupo.campo.data.EstadoSync
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.componentes.BarraConexion

/**
 * Marco común: barra de conexión siempre visible arriba, encabezado con regreso y
 * una zona inferior fija para las acciones principales (al alcance del pulgar).
 */
@Composable
fun Marco(
    repo: Repositorio,
    red: Red,
    titulo: String,
    subtitulo: String? = null,
    atras: (() -> Unit)? = null,
    abrirPendientes: (() -> Unit)? = null,
    acciones: @Composable (() -> Unit)? = null,
    desplazable: Boolean = true,
    contenido: @Composable ColumnScope.() -> Unit,
) {
    val enLinea by red.enLinea.collectAsState()
    val conteos by remember(repo) {
        repo.eventosUi().map { l ->
            l.count { it.estado == EstadoSync.PENDIENTE || it.estado == EstadoSync.ENVIANDO } to l.count { it.estado == EstadoSync.ERROR }
        }
    }.collectAsState(initial = 0 to 0)

    Scaffold(
        containerColor = Colores.Fondo,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            Column(Modifier.statusBarsPadding()) {
                BarraConexion(enLinea, conteos.first, conteos.second, onClick = { abrirPendientes?.invoke() })
                Row(Modifier.fillMaxWidth().padding(start = 4.dp, end = 16.dp, top = 6.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    if (atras != null) {
                        IconButton(onClick = atras) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar", tint = Colores.Tinta) }
                    } else Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(titulo, style = MaterialTheme.typography.titleLarge, color = Colores.Tinta, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        if (subtitulo != null) {
                            Text(subtitulo, style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }
                HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
            }
        },
        bottomBar = {
            if (acciones != null) {
                Column(Modifier.navigationBarsPadding().imePadding()) {
                    HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) { acciones() }
                }
            }
        },
    ) { pad ->
        val base = Modifier.fillMaxSize().padding(pad)
        if (desplazable) {
            Column(base.verticalScroll(rememberScrollState()).padding(horizontal = 16.dp).padding(bottom = 24.dp).let {
                if (acciones == null) it.navigationBarsPadding() else it
            }, content = contenido)
        } else {
            Box(base) { Column(Modifier.fillMaxSize(), content = contenido) }
        }
    }
}

val SinPadding = PaddingValues(0.dp)
