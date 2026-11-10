package mx.grupo.campo.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mx.grupo.campo.BuildConfig

/** Token y datos de la persona, cifrados en el teléfono. Nunca se guarda la contraseña. */
class Sesion(ctx: Context) {
    private val prefs: SharedPreferences = try {
        val llave = MasterKey.Builder(ctx).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(
            ctx, "sesion_cifrada", llave,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    } catch (e: Exception) {
        // Algunos equipos fallan al abrir el almacén cifrado tras restaurar; se reinicia.
        ctx.deleteSharedPreferences("sesion_cifrada")
        ctx.getSharedPreferences("sesion_respaldo", Context.MODE_PRIVATE)
    }

    private val _operador = MutableStateFlow(leerOperador())
    val operador: StateFlow<Operador?> = _operador

    private val _expirada = MutableStateFlow(false)
    val expirada: StateFlow<Boolean> = _expirada

    val token: String? get() = prefs.getString("token", null)
    val usuario: String get() = prefs.getString("usuario", "") ?: ""
    val debeCambiar: Boolean get() = prefs.getBoolean("debe_cambiar", false)

    var urlPuente: String
        get() = prefs.getString("url_puente", null)?.ifBlank { null } ?: BuildConfig.PUENTE_URL
        set(v) = prefs.edit().putString("url_puente", v.trim().trimEnd('/')).apply()

    private fun leerOperador(): Operador? = prefs.getString("operador", null)?.let {
        try { JSON.decodeFromString(Operador.serializer(), it) } catch (_: Exception) { null }
    }

    fun guardar(usuario: String, r: RespuestaLogin) {
        prefs.edit()
            .putString("usuario", usuario)
            .putString("token", r.token)
            .putBoolean("debe_cambiar", r.debeCambiarPassword)
            .putString("operador", r.operador?.let { JSON.encodeToString(Operador.serializer(), it) })
            .apply()
        _operador.value = r.operador
        _expirada.value = false
    }

    fun marcarExpirada() {
        _expirada.value = true
    }

    /** Cierra sesión. Las acciones sin enviar se quedan guardadas y se envían cuando esa persona vuelva a entrar. */
    fun cerrar() {
        prefs.edit().remove("token").remove("operador").remove("debe_cambiar").apply()
        _operador.value = null
        _expirada.value = false
    }
}
