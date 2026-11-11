package mx.grupo.campo.ui.pantallas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import mx.grupo.campo.data.AdjuntoLocal
import mx.grupo.campo.data.Evidencias
import mx.grupo.campo.data.Material
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.data.TareaUi
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.EstiloMono
import mx.grupo.campo.ui.componentes.Aviso
import mx.grupo.campo.ui.componentes.BotonGrande
import mx.grupo.campo.ui.componentes.CapturaFotos
import mx.grupo.campo.ui.componentes.PanelFirma
import mx.grupo.campo.ui.componentes.Seccion
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.UUID

private val HORA = DateTimeFormatter.ofPattern("EEE d MMM · HH:mm", Locale("es", "MX"))

/** Obtiene la tarea y muestra el formulario; si ya no existe, avisa. */
@Composable
private fun ConTarea(repo: Repositorio, red: Red, clave: String, titulo: String, atras: () -> Unit, contenido: @Composable (TareaUi) -> Unit) {
    val ui by remember(clave) { repo.tareaUi(clave) }.collectAsState(initial = null)
    val u = ui
    if (u == null) {
        Marco(repo, red, titulo = titulo, atras = atras) { Text("Cargando…", modifier = Modifier.padding(vertical = 24.dp)) }
    } else contenido(u)
}

private fun fotos(rutas: List<String>, rol: String = "foto") = rutas.map { AdjuntoLocal(rol, it, "image/jpeg") }

@Composable
private fun Faltan(lista: List<String>) {
    if (lista.isNotEmpty()) Text("Falta: " + lista.joinToString(", "), style = MaterialTheme.typography.bodyMedium, color = Colores.Vencido,
        modifier = Modifier.padding(bottom = 4.dp))
}

@Composable
private fun Campo(valor: String, cambio: (String) -> Unit, etiqueta: String, modifier: Modifier = Modifier, lineas: Int = 1,
                  teclado: KeyboardType = KeyboardType.Text, mono: Boolean = false) {
    OutlinedTextField(
        valor, cambio, label = { Text(etiqueta) }, singleLine = lineas == 1, minLines = lineas,
        keyboardOptions = KeyboardOptions(keyboardType = teclado, capitalization = if (teclado == KeyboardType.Text) KeyboardCapitalization.Sentences else KeyboardCapitalization.None),
        textStyle = if (mono) EstiloMono.copy(color = Colores.Tinta) else MaterialTheme.typography.bodyLarge,
        colors = campoTexto(), modifier = modifier.fillMaxWidth(),
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Opciones(opciones: List<String>, elegida: String?, onElegir: (String) -> Unit) {
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        opciones.forEach { o ->
            FilterChip(
                selected = o == elegida, onClick = { onElegir(o) }, label = { Text(o, style = MaterialTheme.typography.bodyLarge) },
                shape = RoundedCornerShape(50),
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Colores.Tinta, selectedLabelColor = Colores.Fondo),
                border = FilterChipDefaults.filterChipBorder(enabled = true, selected = o == elegida, borderColor = Colores.Piedra),
                modifier = Modifier.height(44.dp),
            )
        }
    }
}

/** Botón de firma: al guardar, la firma se escribe como PNG en almacenamiento privado. */
@Composable
private fun SeccionFirma(titulo: String, onFirma: (Bitmap?) -> Unit) {
    Seccion(titulo) { PanelFirma(onFirma) }
}

// ---------------------------------------------------------------- entrega directa

@Composable
fun ConfirmarEntregaPantalla(repo: Repositorio, red: Red, clave: String, atras: () -> Unit) =
    ConTarea(repo, red, clave, "Confirmar entrega", atras) { u ->
        val t = u.tarea
        val ctx = LocalContext.current
        val alcance = rememberCoroutineScope()
        var recibe by rememberSaveable { mutableStateOf(t.recibe ?: "") }
        var rutas by rememberSaveable { mutableStateOf(listOf<String>()) }
        var firma by remember { mutableStateOf<Bitmap?>(null) }
        var obs by rememberSaveable { mutableStateOf("") }
        var guardando by remember { mutableStateOf(false) }
        val ahora = Instant.now()
        val faltan = buildList {
            if (recibe.isBlank()) add("nombre de quien recibe")
            if (rutas.isEmpty()) add("foto")
            if (firma == null) add("firma")
        }
        Marco(
            repo, red, titulo = "Confirmar entrega", subtitulo = "${t.referencia} · ${t.cliente ?: ""}", atras = atras,
            acciones = {
                Faltan(faltan)
                BotonGrande(if (guardando) "Guardando…" else "Confirmar entrega al cliente", Icons.Filled.CheckCircle, color = Colores.Directa,
                    habilitado = faltan.isEmpty() && !guardando, modifier = Modifier.testTag("guardar")) {
                    guardando = true
                    alcance.launch {
                        val f = Evidencias.guardarFirma(ctx, firma!!)
                        repo.registrar(t, "entregado_cliente", buildJsonObject {
                            put("recibe_nombre", recibe.trim())
                            put("fecha_entrega", Instant.now().toString())
                            if (obs.isNotBlank()) put("observaciones", obs.trim())
                        }, fotos(rutas) + AdjuntoLocal("firma", f.path, "image/png"))
                        atras()
                    }
                }
            },
        ) {
            Seccion("Quién recibe") {
                Campo(recibe, { recibe = it }, "Nombre de quien recibe", Modifier.testTag("recibe"))
                Text("Hora de entrega: ${HORA.format(ahora.atZone(ZoneId.systemDefault()))} (se registra al confirmar)",
                    style = EstiloMono.copy(color = Colores.Tinta3))
            }
            Seccion("Evidencia") { CapturaFotos("Foto de la mercancía entregada", rutas, { rutas = it }) }
            SeccionFirma("Firma de quien recibe") { firma = it }
            Seccion("Observaciones (opcional)") { Campo(obs, { obs = it }, "Observaciones", lineas = 2) }
            Text("Se guarda en el teléfono y se envía a Odoo en cuanto haya señal. No se marca como sincronizada hasta que Odoo la confirme.",
                style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3, modifier = Modifier.padding(top = 8.dp))
        }
    }

// ---------------------------------------------------------------- paquetería

private val PAQUETERIAS = listOf("Estafeta", "DHL", "FedEx", "Paquetexpress", "Castores", "Tres Guerras", "Redpack", "99minutos")

@Composable
fun ConfirmarPaqueteriaPantalla(repo: Repositorio, red: Red, clave: String, atras: () -> Unit) =
    ConTarea(repo, red, clave, "Entrega a paquetería", atras) { u ->
        val t = u.tarea
        val ctx = LocalContext.current
        val alcance = rememberCoroutineScope()
        var paq by rememberSaveable { mutableStateOf(t.paqueteria ?: "") }
        var guia by rememberSaveable { mutableStateOf("") }
        var precio by rememberSaveable { mutableStateOf("") }
        var moneda by rememberSaveable { mutableStateOf("MXN") }
        var factura by rememberSaveable { mutableStateOf("") }
        var comprobantes by rememberSaveable { mutableStateOf(listOf<String>()) }
        var fecha by rememberSaveable { mutableStateOf(LocalDateTime.now().withSecond(0).withNano(0).toString()) }
        var guardando by remember { mutableStateOf(false) }
        val fechaLocal = LocalDateTime.parse(fecha)
        val precioNum = precio.replace(",", "").toDoubleOrNull()
        val faltan = buildList {
            if (paq.isBlank()) add("paquetería")
            if (guia.isBlank()) add("guía")
            if (precioNum == null) add("precio")
            if (comprobantes.isEmpty()) add("foto de la nota o recibo")
            if (fechaLocal.isAfter(LocalDateTime.now().plusMinutes(5))) add("fecha válida (no futura)")
        }
        Marco(
            repo, red, titulo = "Entrega a paquetería", subtitulo = "${t.referencia} · ${t.cliente ?: ""}", atras = atras,
            acciones = {
                Faltan(faltan)
                BotonGrande(if (guardando) "Guardando…" else "Registrar entregado a paquetería", Icons.Filled.CheckCircle, color = Colores.Paqueteria,
                    habilitado = faltan.isEmpty() && !guardando, modifier = Modifier.testTag("guardar")) {
                    guardando = true
                    alcance.launch {
                        repo.registrar(t, "entregado_paqueteria", buildJsonObject {
                            put("paqueteria", paq.trim())
                            put("guia", guia.trim())
                            put("precio", precioNum!!)
                            put("moneda", moneda)
                            if (factura.isNotBlank()) put("factura", factura.trim())
                            put("fecha_entrega", fechaLocal.atZone(ZoneId.systemDefault()).toInstant().toString())
                        }, fotos(comprobantes, "comprobante"))
                        atras()
                    }
                }
            },
        ) {
            Aviso("Esto registra «Entregado a paquetería», no «Entregado al cliente». La entrega final la confirma la oficina con el rastreo de la guía.", Colores.Paqueteria)
            Seccion("Paquetería") {
                Opciones(PAQUETERIAS, paq) { paq = it }
                Campo(paq, { paq = it }, "Paquetería (o escribe otra)")
                Campo(guia, { guia = it.trim() }, "Número de guía", Modifier.testTag("guia"), teclado = KeyboardType.Ascii, mono = true)
            }
            Seccion("Pago") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Campo(precio, { precio = it.filter { c -> c.isDigit() || c == '.' || c == ',' } }, "Precio pagado",
                        Modifier.weight(1f).testTag("precio"), teclado = KeyboardType.Decimal, mono = true)
                    Spacer(Modifier.width(10.dp))
                    Opciones(listOf("MXN", "USD"), moneda) { moneda = it }
                }
                Campo(factura, { factura = it }, "Número de factura (opcional)", teclado = KeyboardType.Ascii, mono = true)
            }
            Seccion("Fecha y hora de entrega a la paquetería") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(HORA.format(fechaLocal), style = EstiloMono.copy(color = Colores.Tinta), modifier = Modifier.weight(1f))
                    TextButton(onClick = {
                        DatePickerDialog(ctx, { _, y, m, d ->
                            TimePickerDialog(ctx, { _, h, min ->
                                fecha = LocalDateTime.of(y, m + 1, d, h, min).toString()
                            }, fechaLocal.hour, fechaLocal.minute, true).show()
                        }, fechaLocal.year, fechaLocal.monthValue - 1, fechaLocal.dayOfMonth).show()
                    }) {
                        Icon(Icons.Filled.Schedule, null, tint = Colores.Tinta); Spacer(Modifier.width(6.dp)); Text("Cambiar", color = Colores.Tinta)
                    }
                }
            }
            Seccion("Comprobante") { CapturaFotos("Foto de la nota o recibo", comprobantes, { comprobantes = it }, maximo = 3, tag = "comprobante") }
        }
    }

// ---------------------------------------------------------------- motivo (no entregado / pendiente / incidencia)

private val MOTIVOS = mapOf(
    "no_entregado" to listOf("Cliente ausente", "Dirección incorrecta", "Rechazó la mercancía", "Sin acceso al domicilio", "Mercancía dañada", "Otro"),
    "pendiente" to listOf("Falta material", "Cliente no disponible", "Requiere otra visita", "Falta autorización", "Otro"),
    "incidencia" to listOf("Equipo dañado", "Falta material", "Riesgo de seguridad", "Accidente", "Cliente inconforme", "Otro"),
)
private val TITULO_MOTIVO = mapOf("no_entregado" to "No se pudo entregar", "pendiente" to "Dejar pendiente", "incidencia" to "Reportar incidencia")

@Composable
fun MotivoPantalla(repo: Repositorio, red: Red, clave: String, tipo: String, atras: () -> Unit) =
    ConTarea(repo, red, clave, TITULO_MOTIVO[tipo] ?: "Motivo", atras) { u ->
        val t = u.tarea
        val alcance = rememberCoroutineScope()
        var motivo by rememberSaveable { mutableStateOf<String?>(null) }
        var obs by rememberSaveable { mutableStateOf("") }
        var rutas by rememberSaveable { mutableStateOf(listOf<String>()) }
        var guardando by remember { mutableStateOf(false) }
        val faltan = buildList {
            if (motivo == null) add("motivo")
            if (motivo == "Otro" && obs.isBlank()) add("describe el motivo")
        }
        Marco(
            repo, red, titulo = TITULO_MOTIVO[tipo] ?: "Motivo", subtitulo = "${t.referencia} · ${t.cliente ?: ""}", atras = atras,
            acciones = {
                Faltan(faltan)
                BotonGrande(if (guardando) "Guardando…" else "Registrar", Icons.Filled.Send, color = if (tipo == "pendiente") Colores.Tinta else Colores.Vencido,
                    habilitado = faltan.isEmpty() && !guardando, modifier = Modifier.testTag("guardar")) {
                    guardando = true
                    alcance.launch {
                        val m = if (motivo == "Otro") obs.trim() else motivo!!
                        repo.registrar(t, tipo, buildJsonObject {
                            put("motivo", m)
                            if (obs.isNotBlank() && motivo != "Otro") put("observaciones", obs.trim())
                        }, fotos(rutas))
                        atras()
                    }
                }
            },
        ) {
            if (tipo == "no_entregado") Aviso("La entrega no se marcará como entregada. La oficina verá el motivo en Odoo.", Colores.Vencido)
            Seccion("Motivo") { Opciones(MOTIVOS[tipo] ?: listOf("Otro"), motivo) { motivo = it } }
            Seccion("Observaciones") { Campo(obs, { obs = it }, if (motivo == "Otro") "Describe el motivo" else "Observaciones (opcional)", lineas = 3) }
            Seccion("Fotos (opcional)") { CapturaFotos("Evidencia", rutas, { rutas = it }) }
        }
    }

// ---------------------------------------------------------------- materiales

@Composable
private fun EditorMateriales(lista: MutableList<Material>, tipo: String) {
    var desc by rememberSaveable { mutableStateOf("") }
    var cant by rememberSaveable { mutableStateOf("1") }
    var obs by rememberSaveable { mutableStateOf("") }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        lista.forEachIndexed { i, m ->
            if (i > 0) HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(numero(m.cantidad), style = EstiloMono.copy(color = Colores.Tinta), modifier = Modifier.width(56.dp))
                Column(Modifier.weight(1f)) {
                    Text(m.descripcion, style = MaterialTheme.typography.bodyLarge)
                    m.observaciones?.let { Text(it, style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3) }
                }
                IconButton(onClick = { lista.removeAt(i) }) { Icon(Icons.Filled.Close, "Quitar", tint = Colores.Tinta3) }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Campo(cant, { cant = it.filter { c -> c.isDigit() || c == '.' } }, "Cant.", Modifier.width(96.dp), teclado = KeyboardType.Decimal, mono = true)
            Spacer(Modifier.width(8.dp))
            Campo(desc, { desc = it }, "Material", Modifier.weight(1f).testTag("material_desc"))
        }
        Campo(obs, { obs = it }, "Observaciones (opcional)")
        val c = cant.toDoubleOrNull()
        BotonGrande("Agregar a la lista", Icons.Filled.Add, color = Colores.Tinta, contorno = true, habilitado = desc.isNotBlank() && c != null && c > 0) {
            lista.add(Material(uuid = UUID.randomUUID().toString(), descripcion = desc.trim(), cantidad = c!!, observaciones = obs.trim().ifBlank { null }, tipo = tipo))
            desc = ""; cant = "1"; obs = ""
        }
    }
}

private fun jsonMateriales(lista: List<Material>) = buildJsonArray {
    lista.forEach { m ->
        add(buildJsonObject {
            put("uuid", m.uuid)
            put("descripcion", m.descripcion)
            put("cantidad", m.cantidad)
            m.observaciones?.let { put("observaciones", it) }
        })
    }
}

@Composable
fun MaterialesPantalla(repo: Repositorio, red: Red, clave: String, atras: () -> Unit) =
    ConTarea(repo, red, clave, "Materiales", atras) { u ->
        val t = u.tarea
        val alcance = rememberCoroutineScope()
        var tipo by rememberSaveable { mutableStateOf("solicitado") }
        val lista = remember { mutableStateListOf<Material>() }
        var guardando by remember { mutableStateOf(false) }
        Marco(
            repo, red, titulo = "Materiales", subtitulo = "${t.referencia} · ${t.cliente ?: ""}", atras = atras,
            acciones = {
                BotonGrande(if (guardando) "Guardando…" else if (tipo == "solicitado") "Enviar solicitud (${lista.size})" else "Registrar utilizados (${lista.size})",
                    Icons.Filled.Send, color = Colores.Servicio, habilitado = lista.isNotEmpty() && !guardando, modifier = Modifier.testTag("guardar")) {
                    guardando = true
                    alcance.launch {
                        repo.registrar(t, if (tipo == "solicitado") "materiales_solicitados" else "materiales_utilizados",
                            buildJsonObject { put("materiales", jsonMateriales(lista.map { it.copy(tipo = tipo) })) })
                        atras()
                    }
                }
            },
        ) {
            Seccion("¿Qué vas a registrar?") {
                Opciones(listOf("Solicitar material", "Material utilizado"), if (tipo == "solicitado") "Solicitar material" else "Material utilizado") {
                    tipo = if (it == "Solicitar material") "solicitado" else "utilizado"
                }
                Text(
                    if (tipo == "solicitado") "La solicitud queda en Odoo como «solicitado». La oficina la autoriza ahí; verás el estado aquí."
                    else "Lo usado queda registrado en la tarea de Odoo junto a lo solicitado.",
                    style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3,
                )
            }
            Seccion("Lista") { EditorMateriales(lista, tipo) }
            val previos = t.materiales
            if (previos.isNotEmpty()) Seccion("Ya registrados en Odoo") {
                previos.forEach { m ->
                    Row {
                        Text(numero(m.cantidad), style = EstiloMono.copy(color = Colores.Tinta), modifier = Modifier.width(56.dp))
                        Text(m.descripcion, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        Text((m.tipo + (m.estado?.let { " · $it" } ?: "")).uppercase(), style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
                    }
                }
            }
        }
    }

// ---------------------------------------------------------------- finalizar servicio

@Composable
fun FinalizarServicioPantalla(repo: Repositorio, red: Red, clave: String, atras: () -> Unit) =
    ConTarea(repo, red, clave, "Finalizar servicio", atras) { u ->
        val t = u.tarea
        val ctx = LocalContext.current
        val alcance = rememberCoroutineScope()
        var trabajo by rememberSaveable { mutableStateOf("") }
        var firmaNombre by rememberSaveable { mutableStateOf("") }
        var rutas by rememberSaveable { mutableStateOf(listOf<String>()) }
        var firma by remember { mutableStateOf<Bitmap?>(null) }
        val usados = remember { mutableStateListOf<Material>() }
        var guardando by remember { mutableStateOf(false) }
        val faltan = buildList {
            if (trabajo.isBlank()) add("trabajo realizado")
            if (rutas.isEmpty()) add("foto")
            if (firmaNombre.isBlank()) add("nombre de quien firma")
            if (firma == null) add("firma")
        }
        Marco(
            repo, red, titulo = "Finalizar servicio", subtitulo = "${t.referencia} · ${t.cliente ?: ""}", atras = atras,
            acciones = {
                Faltan(faltan)
                BotonGrande(if (guardando) "Guardando…" else "Finalizar servicio", Icons.Filled.CheckCircle, color = Colores.Servicio,
                    habilitado = faltan.isEmpty() && !guardando, modifier = Modifier.testTag("guardar")) {
                    guardando = true
                    alcance.launch {
                        val f = Evidencias.guardarFirma(ctx, firma!!)
                        repo.registrar(t, "finalizar", buildJsonObject {
                            put("trabajo_realizado", trabajo.trim())
                            put("firma_nombre", firmaNombre.trim())
                            put("fecha_entrega", Instant.now().toString())
                            put("materiales_utilizados", jsonMateriales(usados))
                        }, fotos(rutas) + AdjuntoLocal("firma", f.path, "image/png"))
                        atras()
                    }
                }
            },
        ) {
            Seccion("Trabajo realizado") { Campo(trabajo, { trabajo = it }, "Describe lo que hiciste", Modifier.testTag("trabajo"), lineas = 4) }
            Seccion("Material utilizado (opcional)") { EditorMateriales(usados, "utilizado") }
            Seccion("Evidencia") { CapturaFotos("Fotos del trabajo terminado", rutas, { rutas = it }) }
            Seccion("Conformidad del cliente") { Campo(firmaNombre, { firmaNombre = it }, "Nombre de quien firma", Modifier.testTag("firma_nombre")) }
            SeccionFirma("Firma del cliente") { firma = it }
        }
    }
