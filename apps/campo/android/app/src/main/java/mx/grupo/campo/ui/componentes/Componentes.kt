package mx.grupo.campo.ui.componentes

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mx.grupo.campo.data.EstadoSla
import mx.grupo.campo.data.EstadoSync
import mx.grupo.campo.data.Evidencias
import mx.grupo.campo.data.Tarea
import mx.grupo.campo.ui.Colores
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FORMATO = DateTimeFormatter.ofPattern("EEE d MMM, HH:mm", Locale("es", "MX"))

fun fechaCorta(iso: String?): String? = try {
    iso?.let { FORMATO.format(Instant.parse(it).atZone(ZoneId.systemDefault())) }
} catch (_: Exception) {
    null
}

// ---------- barra de conexión ----------

@Composable
fun BarraConexion(enLinea: Boolean, pendientes: Int, errores: Int, onClick: () -> Unit) {
    val (color, icono, texto) = when {
        !enLinea -> Triple(Colores.Tinta3, Icons.Filled.CloudOff, if (pendientes > 0) "Sin conexión · $pendientes por enviar (se guardan en el teléfono)" else "Sin conexión · trabajando con datos guardados")
        errores > 0 -> Triple(Colores.Rojo, Icons.Filled.ErrorOutline, "$errores acciones con error · toca para revisar")
        pendientes > 0 -> Triple(Colores.Tinta2, Icons.Filled.CloudUpload, "Enviando $pendientes acciones a Odoo…")
        else -> Triple(Colores.ATiempo, Icons.Filled.CloudDone, "En línea · todo sincronizado")
    }
    Surface(color = color, modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).testTag("barra_conexion")) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icono, null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(texto, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ---------- chips ----------

@Composable
fun Etiqueta(texto: String, color: Color, relleno: Boolean = false, modifier: Modifier = Modifier) {
    // Tablero denso: punto de color + texto mono; el relleno solo para el tipo de tarea.
    if (relleno) {
        Surface(shape = RoundedCornerShape(50), color = color, modifier = modifier) {
            Text(texto, color = Color.White, style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp))
        }
    } else {
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(8.dp).clip(RoundedCornerShape(50)).background(color))
            Spacer(Modifier.width(6.dp))
            Text(texto, color = Colores.Tinta2, style = MaterialTheme.typography.labelMedium)
        }
    }
}

fun nombreModalidad(t: Tarea): String = when {
    t.esServicio -> if (t.tipoServicio == "reparacion") "REPARACIÓN" else "INSTALACIÓN"
    t.esPaqueteria -> "PAQUETERÍA"
    else -> "TRANSPORTE PROPIO"
}

@Composable
fun ChipModalidad(t: Tarea) = Etiqueta(nombreModalidad(t), Colores.deTarea(t), relleno = true)

@Composable
fun ChipSla(e: EstadoSla) = Etiqueta(e.texto, Colores.deSemaforo(e.semaforo))

@Composable
fun ChipSync(sync: String?, pendientes: Int) {
    when (sync) {
        null -> Unit
        EstadoSync.SINCRONIZADO -> Etiqueta("Sincronizado", Colores.Verde)
        EstadoSync.PENDIENTE -> Etiqueta("Pendiente de enviar ($pendientes)", Colores.Ambar)
        EstadoSync.ERROR -> Etiqueta("Error al enviar", Colores.Rojo)
        EstadoSync.RECHAZADO -> Etiqueta("Rechazado por oficina", Colores.Rojo)
    }
}

// ---------- botones y datos ----------

@Composable
fun BotonGrande(
    texto: String,
    icono: ImageVector?,
    color: Color = Colores.Tinta,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    contorno: Boolean = false,
    onClick: () -> Unit,
) {
    // CTA en píldora; feedback de presión con escala 0.97 en 100 ms.
    val interaccion = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val presionado by interaccion.collectIsPressedAsState()
    val escala by androidx.compose.animation.core.animateFloatAsState(if (presionado) 0.97f else 1f, androidx.compose.animation.core.tween(100), label = "press")
    val mod = modifier.fillMaxWidth().heightIn(min = 58.dp).graphicsLayer { scaleX = escala; scaleY = escala }
    val contenido: @Composable () -> Unit = {
        if (icono != null) {
            Icon(icono, null, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(10.dp))
        }
        Text(texto, style = MaterialTheme.typography.labelLarge)
    }
    if (contorno) {
        OutlinedButton(onClick, mod, enabled = habilitado, shape = RoundedCornerShape(50), border = BorderStroke(1.5.dp, color),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color), interactionSource = interaccion) { contenido() }
    } else {
        Button(onClick, mod, enabled = habilitado, shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = color, contentColor = Color.White,
                disabledContainerColor = Colores.Piedra.copy(alpha = 0.35f), disabledContentColor = Colores.Tinta3),
            interactionSource = interaccion) { contenido() }
    }
}

@Composable
fun Seccion(titulo: String, content: @Composable () -> Unit) {
    // Sin tarjetas: rótulo mono + separador de 1 px (arquetipo tablero denso).
    Column(Modifier.fillMaxWidth()) {
        androidx.compose.material3.HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
        Column(Modifier.padding(vertical = 14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(titulo.uppercase(), style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
            content()
        }
    }
}

@Composable
fun Dato(etiqueta: String, valor: String?, destacado: Boolean = false, mono: Boolean = false) {
    if (valor.isNullOrBlank()) return
    // Etiqueta pegada a su valor.
    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
        Text(etiqueta, fontSize = 13.sp, color = Colores.Tinta3)
        Text(
            valor,
            style = when {
                mono -> mx.grupo.campo.ui.EstiloMono.copy(fontSize = 16.sp, color = Colores.Tinta)
                destacado -> MaterialTheme.typography.titleMedium
                else -> MaterialTheme.typography.bodyLarge
            },
        )
    }
}

@Composable
fun Aviso(texto: String, color: Color) {
    Surface(color = color.copy(alpha = 0.08f), shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Block, null, tint = color, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(texto, color = Colores.Tinta, fontSize = 15.sp)
        }
    }
}

// ---------- intents ----------

fun abrirNavegacion(ctx: Context, t: Tarea) {
    val d = t.direccion
    val uri = if (d?.lat != null && d.lon != null) Uri.parse("geo:${d.lat},${d.lon}?q=${d.lat},${d.lon}(${Uri.encode(d.nombre)})")
    else Uri.parse("geo:0,0?q=" + Uri.encode(d?.texto ?: ""))
    try {
        ctx.startActivity(Intent.createChooser(Intent(Intent.ACTION_VIEW, uri), "Abrir con"))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(ctx, "No hay app de mapas instalada", Toast.LENGTH_LONG).show()
    }
}

fun llamar(ctx: Context, telefono: String) {
    try {
        ctx.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + telefono.filter { it.isDigit() || it == '+' })))
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(ctx, "No se puede marcar desde este equipo", Toast.LENGTH_LONG).show()
    }
}

// ---------- fotos ----------

/** Captura con la cámara del teléfono. Guarda las rutas en estado que sobrevive si Android cierra la app mientras la cámara está abierta. */
@Composable
fun CapturaFotos(titulo: String, rutas: List<String>, onCambio: (List<String>) -> Unit, maximo: Int = 6, tag: String = "fotos") {
    val ctx = LocalContext.current
    var pendiente by rememberSaveable { mutableStateOf<String?>(null) }
    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { ok ->
        val f = pendiente?.let { File(it) }
        pendiente = null
        if (ok && f != null) {
            val comprimida = Evidencias.comprimir(ctx, f)
            if (comprimida != null) onCambio(rutas + comprimida.path)
            else Toast.makeText(ctx, "No se pudo leer la foto, intenta de nuevo", Toast.LENGTH_LONG).show()
        } else f?.delete()
    }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(titulo, style = MaterialTheme.typography.titleMedium)
        Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            rutas.forEach { r ->
                Box {
                    val bmp = remember(r) { BitmapFactory.decodeFile(r, BitmapFactory.Options().apply { inSampleSize = 8 }) }
                    if (bmp != null) {
                        Image(bmp.asImageBitmap(), null, contentScale = ContentScale.Crop,
                            modifier = Modifier.size(96.dp).clip(RoundedCornerShape(10.dp)))
                    }
                    IconButton(onClick = { File(r).delete(); onCambio(rutas - r) },
                        modifier = Modifier.align(Alignment.TopEnd).size(32.dp).background(Color(0x99000000), RoundedCornerShape(16.dp))) {
                        Icon(Icons.Filled.Close, "Quitar foto", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
            if (rutas.size < maximo) {
                Box(
                    Modifier.size(96.dp).clip(RoundedCornerShape(10.dp)).border(1.5.dp, Colores.Gris, RoundedCornerShape(10.dp))
                        .clickable {
                            val (f, uri) = Evidencias.nuevaCaptura(ctx)
                            pendiente = f.path
                            try { camara.launch(uri) } catch (_: ActivityNotFoundException) {
                                Toast.makeText(ctx, "No hay cámara disponible", Toast.LENGTH_LONG).show()
                            }
                        }
                        .testTag(tag),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Filled.AddAPhoto, null, tint = Colores.Gris, modifier = Modifier.size(32.dp))
                        Text("Tomar foto", fontSize = 12.sp, color = Colores.Gris)
                    }
                }
            }
        }
    }
}

// ---------- firma ----------

/** Panel para firmar con el dedo. Devuelve la firma como imagen al soltar cada trazo. */
@Composable
fun PanelFirma(onFirma: (Bitmap?) -> Unit, tag: String = "firma") {
    val trazos = remember { mutableStateListOf<List<Offset>>() }
    var actual by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var tam by remember { mutableStateOf(IntSize.Zero) }

    fun exportar() {
        if (trazos.isEmpty() || tam.width == 0) { onFirma(null); return }
        val bmp = Bitmap.createBitmap(tam.width, tam.height, Bitmap.Config.ARGB_8888)
        val c = android.graphics.Canvas(bmp)
        c.drawColor(android.graphics.Color.WHITE)
        val p = Paint().apply { color = android.graphics.Color.BLACK; strokeWidth = 6f; style = Paint.Style.STROKE; isAntiAlias = true; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND }
        for (t in trazos) {
            val path = android.graphics.Path()
            t.forEachIndexed { i, o -> if (i == 0) path.moveTo(o.x, o.y) else path.lineTo(o.x, o.y) }
            if (t.size == 1) path.lineTo(t[0].x + 1, t[0].y + 1)
            c.drawPath(path, p)
        }
        onFirma(bmp)
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(
            Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(10.dp)).background(Color.White)
                .border(1.5.dp, Colores.Gris, RoundedCornerShape(10.dp))
                .onSizeChanged { tam = it }
                .testTag(tag)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { actual = listOf(it) },
                        onDrag = { change, _ -> actual = actual + change.position },
                        onDragEnd = { trazos.add(actual); actual = emptyList(); exportar() },
                    )
                },
        ) {
            Canvas(Modifier.matchParentSizeCompat()) {
                val estilo = Stroke(width = 6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                (trazos + listOf(actual)).forEach { t ->
                    if (t.isEmpty()) return@forEach
                    val path = Path().apply { moveTo(t[0].x, t[0].y); t.drop(1).forEach { lineTo(it.x, it.y) } }
                    drawPath(path, Color.Black, style = estilo)
                }
            }
            if (trazos.isEmpty() && actual.isEmpty()) {
                Text("Firme aquí con el dedo", color = Colores.Gris, modifier = Modifier.align(Alignment.Center))
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = { trazos.clear(); actual = emptyList(); onFirma(null) }) { Text("Borrar firma") }
        }
    }
}

private fun Modifier.matchParentSizeCompat(): Modifier = this.then(Modifier.fillMaxWidth().height(200.dp))
