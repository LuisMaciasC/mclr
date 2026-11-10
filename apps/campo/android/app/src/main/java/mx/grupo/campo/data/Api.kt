package mx.grupo.campo.data

import kotlinx.serialization.json.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.TimeUnit

/** Error que viene del puente con un código y mensaje en español. */
class PuenteException(val http: Int, val codigo: String, override val message: String, val reintentar: Boolean) : Exception(message)

/** Cliente HTTP del puente. Sin credenciales de Odoo: solo el token de la persona. */
class Api(private val sesion: Sesion, private val cliente: OkHttpClient = clienteDefault) {

    companion object {
        val clienteDefault: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .build()
        private val TIPO_JSON = "application/json; charset=utf-8".toMediaType()
    }

    private fun peticion(ruta: String): Request.Builder {
        val b = Request.Builder().url(sesion.urlPuente + ruta)
        sesion.token?.let { b.header("Authorization", "Bearer $it") }
        return b
    }

    @Throws(IOException::class, PuenteException::class)
    private fun ejecutar(req: Request, aceptar409: Boolean = false): String {
        cliente.newCall(req).execute().use { r ->
            val cuerpo = r.body?.string().orEmpty()
            if (r.isSuccessful || (aceptar409 && r.code == 409 && cuerpo.contains("\"resultado\""))) return cuerpo
            val err = try { JSON.decodeFromString(ErrorPuente.serializer(), cuerpo) } catch (_: Exception) { null }
            throw PuenteException(
                r.code,
                err?.codigo ?: "HTTP_${r.code}",
                err?.mensaje?.ifBlank { null } ?: "El servidor respondió ${r.code}",
                err?.reintentar ?: (r.code >= 500),
            )
        }
    }

    fun login(usuario: String, password: String): RespuestaLogin {
        val body = """{"usuario":${q(usuario)},"password":${q(password)}}""".toRequestBody(TIPO_JSON)
        return JSON.decodeFromString(RespuestaLogin.serializer(), ejecutar(peticion("/v1/sesion").post(body).build()))
    }

    fun cambiarPassword(usuario: String, nueva: String): RespuestaLogin {
        val body = """{"usuario":${q(usuario)},"password_nueva":${q(nueva)}}""".toRequestBody(TIPO_JSON)
        return JSON.decodeFromString(RespuestaLogin.serializer(), ejecutar(peticion("/v1/sesion/password").post(body).build()))
    }

    fun tareas(): RespuestaTareas =
        JSON.decodeFromString(RespuestaTareas.serializer(), ejecutar(peticion("/v1/tareas").get().build()))

    /** Envía un evento. Un 409 con "resultado" es una respuesta válida (rechazado por la oficina), no un error técnico. */
    fun enviarEvento(cuerpo: JsonObject): RespuestaEvento {
        val body = cuerpo.toString().toRequestBody(TIPO_JSON)
        return JSON.decodeFromString(RespuestaEvento.serializer(), ejecutar(peticion("/v1/eventos").post(body).build(), aceptar409 = true))
    }

    private fun q(s: String): String = kotlinx.serialization.json.JsonPrimitive(s).toString()
}
