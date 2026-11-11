package mx.grupo.campo.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import mx.grupo.campo.R
import mx.grupo.campo.data.Semaforo
import mx.grupo.campo.data.Tarea

/*
 * Dirección (agente de diseño, DISENO-BASE.md):
 *   transporte · arquetipo tablero-denso · paleta espresso (fondo #F0F2F4, tinta #170A07)
 *   · tipo Schibsted Grotesk + JetBrains Mono · firma: cuenta regresiva del SLA en mono tabular
 *     y línea de tiempo que se dibuja con stagger.
 * Colores de tipo y estado tomados de otras paletas medidas de transporte:
 *   Puerto #1F577B (directa), Flota naranja #F25104 (paquetería), Verde ruta #1D944E (servicio),
 *   Sala nocturna #2D7871 / #943334 (a tiempo / vencido), Durazno Orbix #D59F6A (por vencer).
 */
object Colores {
    // Paleta espresso (medida): fondo, tintas y piedra.
    val Fondo = Color(0xFFF0F2F4)
    val Tinta = Color(0xFF170A07)
    val Tinta2 = Color(0xFF3C1B13)
    val Tinta3 = Color(0xFF59342A)
    val Piedra = Color(0xFFAA9C8F)
    val Linea = Color(0x66AA9C8F) // hairline: piedra al 40 %

    // Tipo de tarea (el color solo marca el tipo y el estado; ocupa poco).
    val Directa = Color(0xFF1F577B)
    val Paqueteria = Color(0xFFF25104)
    val PaqueteriaPresionado = Color(0xFFCB3A03)
    val Servicio = Color(0xFF1D944E)

    // Estado del SLA.
    val ATiempo = Color(0xFF2D7871)
    val PorVencer = Color(0xFFD59F6A)
    val Vencido = Color(0xFF943334)

    // Alias usados por los componentes.
    val Verde = ATiempo
    val Ambar = PorVencer
    val Rojo = Vencido
    val Gris = Piedra
    val Texto = Tinta

    fun deTarea(t: Tarea): Color = when {
        t.esServicio -> Servicio
        t.esPaqueteria -> Paqueteria
        else -> Directa
    }

    fun deSemaforo(s: Semaforo): Color = when (s) {
        Semaforo.VERDE, Semaforo.CUMPLIDO_A_TIEMPO -> ATiempo
        Semaforo.AMBAR -> PorVencer
        Semaforo.ROJO, Semaforo.CUMPLIDO_TARDE -> Vencido
        Semaforo.SIN_FECHA, Semaforo.PAUSADO -> Piedra
    }
}

@OptIn(ExperimentalTextApi::class)
private fun variable(id: Int, peso: Int) =
    Font(id, weight = FontWeight(peso), variationSettings = FontVariation.Settings(FontVariation.weight(peso)))

val Grotesk = FontFamily(
    variable(R.font.schibsted_grotesk, 400),
    variable(R.font.schibsted_grotesk, 500),
    variable(R.font.schibsted_grotesk, 600),
    variable(R.font.schibsted_grotesk, 700),
    variable(R.font.schibsted_grotesk, 800),
)

val Mono = FontFamily(
    variable(R.font.jetbrains_mono, 400),
    variable(R.font.jetbrains_mono, 500),
    variable(R.font.jetbrains_mono, 700),
)

/** Cifras e IDs: mono con números tabulares (no saltan al cambiar). */
val EstiloMono = TextStyle(fontFamily = Mono, fontFeatureSettings = "tnum", fontSize = 14.sp)

private val esquema = lightColorScheme(
    primary = Colores.Tinta,
    onPrimary = Colores.Fondo,
    secondary = Colores.Tinta3,
    background = Colores.Fondo,
    onBackground = Colores.Tinta,
    surface = Colores.Fondo,
    onSurface = Colores.Tinta,
    surfaceVariant = Color(0xFFE6E3E0),
    onSurfaceVariant = Colores.Tinta3,
    outline = Colores.Piedra,
    error = Colores.Vencido,
)

private val tipografia = Typography(
    displayLarge = TextStyle(fontFamily = Mono, fontFeatureSettings = "tnum", fontSize = 56.sp, fontWeight = FontWeight.Bold, letterSpacing = (-1).sp),
    headlineMedium = TextStyle(fontFamily = Grotesk, fontSize = 28.sp, fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp),
    titleLarge = TextStyle(fontFamily = Grotesk, fontSize = 22.sp, fontWeight = FontWeight.Bold),
    titleMedium = TextStyle(fontFamily = Grotesk, fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge = TextStyle(fontFamily = Grotesk, fontSize = 17.sp),
    bodyMedium = TextStyle(fontFamily = Grotesk, fontSize = 15.sp),
    labelLarge = TextStyle(fontFamily = Grotesk, fontSize = 17.sp, fontWeight = FontWeight.SemiBold),
    labelMedium = TextStyle(fontFamily = Mono, fontSize = 12.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.6.sp),
)

@Composable
fun CampoTema(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = esquema, typography = tipografia, content = content)
}
