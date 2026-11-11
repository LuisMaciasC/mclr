package mx.grupo.campo.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Indicador de conexión para la barra superior. */
class Red(ctx: Context) {
    private val cm = ctx.getSystemService(ConnectivityManager::class.java)
    private val _enLinea = MutableStateFlow(hayRed())
    val enLinea: StateFlow<Boolean> = _enLinea
    var alVolver: (() -> Unit)? = null

    init {
        cm.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val antes = _enLinea.value
                _enLinea.value = true
                if (!antes) alVolver?.invoke()
            }
            override fun onLost(network: Network) {
                _enLinea.value = hayRed()
            }
        })
    }

    private fun hayRed(): Boolean {
        val caps = cm.getNetworkCapabilities(cm.activeNetwork) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

/** Última ubicación conocida, solo si la persona dio permiso. Es opcional: nunca bloquea una acción. */
object Ubicacion {
    @SuppressLint("MissingPermission")
    fun ultima(ctx: Context): Location? = try {
        val lm = ctx.getSystemService(LocationManager::class.java)
        listOf(LocationManager.GPS_PROVIDER, LocationManager.NETWORK_PROVIDER, LocationManager.PASSIVE_PROVIDER)
            .mapNotNull { p -> runCatching { lm.getLastKnownLocation(p) }.getOrNull() }
            .filter { System.currentTimeMillis() - it.time < 30 * 60_000 }
            .maxByOrNull { it.time }
    } catch (_: SecurityException) {
        null
    }
}
