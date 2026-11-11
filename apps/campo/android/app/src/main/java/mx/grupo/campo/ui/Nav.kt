package mx.grupo.campo.ui

import android.net.Uri
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.ui.pantallas.CambiarPasswordPantalla
import mx.grupo.campo.ui.pantallas.ConfirmarEntregaPantalla
import mx.grupo.campo.ui.pantallas.ConfirmarPaqueteriaPantalla
import mx.grupo.campo.ui.pantallas.DetallePantalla
import mx.grupo.campo.ui.pantallas.FinalizarServicioPantalla
import mx.grupo.campo.ui.pantallas.LoginPantalla
import mx.grupo.campo.ui.pantallas.MaterialesPantalla
import mx.grupo.campo.ui.pantallas.MotivoPantalla
import mx.grupo.campo.ui.pantallas.PendientesPantalla
import mx.grupo.campo.ui.pantallas.TareasPantalla

/** Curva de la dirección de diseño: salida rápida, llegada suave. */
val Suave = CubicBezierEasing(0.23f, 1f, 0.32f, 1f)

object Rutas {
    const val LOGIN = "login"
    const val PASSWORD = "password"
    const val TAREAS = "tareas"
    const val PENDIENTES = "pendientes"
    fun detalle(clave: String) = "tarea/${Uri.encode(clave)}"
    fun entrega(clave: String) = "entrega/${Uri.encode(clave)}"
    fun paqueteria(clave: String) = "paqueteria/${Uri.encode(clave)}"
    fun motivo(clave: String, tipo: String) = "motivo/${Uri.encode(clave)}/$tipo"
    fun materiales(clave: String) = "materiales/${Uri.encode(clave)}"
    fun finalizar(clave: String) = "finalizar/${Uri.encode(clave)}"
}

@Composable
fun CampoNav(repo: Repositorio, red: Red) {
    val nav = rememberNavController()
    val operador by repo.sesion.operador.collectAsState()
    val inicio = when {
        operador == null || repo.sesion.token == null -> Rutas.LOGIN
        repo.sesion.debeCambiar -> Rutas.PASSWORD
        else -> Rutas.TAREAS
    }

    // Si la sesión se cierra (o el servidor la invalida y la persona sale), vuelve al inicio de sesión.
    LaunchedEffect(operador) {
        if (operador == null && nav.currentDestination?.route != Rutas.LOGIN) {
            nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } }
        }
    }

    val dur = 220
    NavHost(
        nav, startDestination = inicio,
        enterTransition = { slideInHorizontally(tween(dur, easing = Suave)) { it / 6 } + fadeIn(tween(dur, easing = Suave)) },
        exitTransition = { fadeOut(tween(150)) },
        popEnterTransition = { fadeIn(tween(dur, easing = Suave)) },
        popExitTransition = { slideOutHorizontally(tween(dur, easing = Suave)) { it / 6 } + fadeOut(tween(150)) },
    ) {
        composable(Rutas.LOGIN) {
            LoginPantalla(repo, alEntrar = { debeCambiar ->
                nav.navigate(if (debeCambiar) Rutas.PASSWORD else Rutas.TAREAS) { popUpTo(0) { inclusive = true } }
            })
        }
        composable(Rutas.PASSWORD) {
            CambiarPasswordPantalla(repo, alTerminar = { nav.navigate(Rutas.TAREAS) { popUpTo(0) { inclusive = true } } },
                alSalir = { repo.sesion.cerrar() })
        }
        composable(Rutas.TAREAS) {
            TareasPantalla(repo, red, abrir = { nav.navigate(Rutas.detalle(it)) }, abrirPendientes = { nav.navigate(Rutas.PENDIENTES) })
        }
        composable(Rutas.PENDIENTES) {
            PendientesPantalla(repo, red, atras = { nav.popBackStack() }, abrir = { nav.navigate(Rutas.detalle(it)) })
        }
        composable("tarea/{clave}") { e ->
            DetallePantalla(repo, red, clave(e.arguments?.getString("clave")), nav)
        }
        composable("entrega/{clave}") { e ->
            ConfirmarEntregaPantalla(repo, red, clave(e.arguments?.getString("clave")), atras = { volverADetalle(nav) })
        }
        composable("paqueteria/{clave}") { e ->
            ConfirmarPaqueteriaPantalla(repo, red, clave(e.arguments?.getString("clave")), atras = { volverADetalle(nav) })
        }
        composable("motivo/{clave}/{tipo}") { e ->
            MotivoPantalla(repo, red, clave(e.arguments?.getString("clave")), e.arguments?.getString("tipo") ?: "incidencia",
                atras = { volverADetalle(nav) })
        }
        composable("materiales/{clave}") { e ->
            MaterialesPantalla(repo, red, clave(e.arguments?.getString("clave")), atras = { volverADetalle(nav) })
        }
        composable("finalizar/{clave}") { e ->
            FinalizarServicioPantalla(repo, red, clave(e.arguments?.getString("clave")), atras = { volverADetalle(nav) })
        }
    }
}

private fun clave(s: String?): String = Uri.decode(s ?: "")

private fun volverADetalle(nav: NavHostController) {
    nav.popBackStack()
}
