package mx.grupo.campo.ui.pantallas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mx.grupo.campo.BuildConfig
import mx.grupo.campo.data.PuenteException
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.ui.Colores
import mx.grupo.campo.ui.EstiloMono
import mx.grupo.campo.ui.componentes.Aviso
import mx.grupo.campo.ui.componentes.BotonGrande

fun mensajeDeError(e: Throwable): String = when (e) {
    is PuenteException -> e.message
    is java.io.IOException -> "No hay conexión con el servidor. Revisa tus datos o Wi-Fi e intenta de nuevo."
    else -> e.message ?: "Ocurrió un error inesperado."
}

@Composable
fun campoTexto() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Colores.Tinta,
    unfocusedBorderColor = Colores.Piedra,
    focusedLabelColor = Colores.Tinta,
    cursorColor = Colores.Tinta,
    focusedContainerColor = Colores.Fondo,
    unfocusedContainerColor = Colores.Fondo,
)

@Composable
fun LoginPantalla(repo: Repositorio, alEntrar: (debeCambiar: Boolean) -> Unit) {
    var usuario by rememberSaveable { mutableStateOf(repo.sesion.usuario) }
    var password by remember { mutableStateOf("") }
    var ver by remember { mutableStateOf(false) }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var deOtros by remember { mutableIntStateOf(0) }
    var servidor by rememberSaveable { mutableStateOf(false) }
    var url by rememberSaveable { mutableStateOf(repo.sesion.urlPuente) }
    val alcance = rememberCoroutineScope()

    LaunchedEffect(Unit) { deOtros = repo.pendientesDeOtrasPersonas() }

    fun entrar() {
        if (usuario.isBlank() || password.isEmpty() || cargando) return
        cargando = true; error = null
        alcance.launch {
            try {
                val r = repo.entrar(usuario, password)
                alEntrar(r.debeCambiarPassword)
            } catch (e: Exception) {
                error = mensajeDeError(e)
            } finally {
                cargando = false
            }
        }
    }

    Surface(color = Colores.Fondo, modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.systemBarsPadding().imePadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("CAMPO · ENTREGAS Y SERVICIOS", style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
            Text("Inicia sesión", style = MaterialTheme.typography.headlineMedium, color = Colores.Tinta)
            Text("Usa el usuario y la contraseña que te dio la oficina. No es tu cuenta de Odoo.",
                style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                usuario, { usuario = it.trim() }, label = { Text("Usuario") }, singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii, imeAction = ImeAction.Next, autoCorrectEnabled = false),
                colors = campoTexto(), modifier = Modifier.fillMaxWidth().testTag("usuario"),
            )
            OutlinedTextField(
                password, { password = it }, label = { Text("Contraseña") }, singleLine = true,
                visualTransformation = if (ver) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { ver = !ver }) {
                        Icon(if (ver) Icons.Filled.VisibilityOff else Icons.Filled.Visibility, if (ver) "Ocultar" else "Mostrar")
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { entrar() }),
                colors = campoTexto(), modifier = Modifier.fillMaxWidth().testTag("password"),
            )

            error?.let { Aviso(it, Colores.Vencido) }
            if (deOtros > 0) {
                Aviso("En este teléfono hay $deOtros acciones de otra persona sin enviar. Se enviarán cuando esa persona vuelva a iniciar sesión.", Colores.PorVencer)
            }

            BotonGrande(if (cargando) "Entrando…" else "Entrar", Icons.AutoMirrored.Filled.Login,
                habilitado = !cargando && usuario.isNotBlank() && password.isNotEmpty(), modifier = Modifier.testTag("entrar")) { entrar() }

            Spacer(Modifier.height(24.dp))
            HorizontalDivider(thickness = 1.dp, color = Colores.Linea)
            TextButton(onClick = { servidor = !servidor }) {
                Text(if (servidor) "Ocultar servidor" else "Servidor", color = Colores.Tinta3, style = MaterialTheme.typography.labelMedium)
            }
            if (servidor) {
                OutlinedTextField(url, { url = it }, label = { Text("Dirección del servidor") }, singleLine = true,
                    textStyle = EstiloMono, colors = campoTexto(), modifier = Modifier.fillMaxWidth())
                BotonGrande("Guardar servidor", Icons.Filled.Check, contorno = true, habilitado = url.startsWith("https://")) {
                    repo.sesion.urlPuente = url
                }
                Text("Solo cámbialo si la oficina te lo pide. Debe empezar con https://", style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3)
            }
            Text("v${BuildConfig.VERSION_NAME}", style = EstiloMono.copy(color = Colores.Piedra))
        }
    }
}

@Composable
fun CambiarPasswordPantalla(repo: Repositorio, alTerminar: () -> Unit, alSalir: () -> Unit) {
    var nueva by remember { mutableStateOf("") }
    var otra by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    val alcance = rememberCoroutineScope()
    val valida = nueva.length >= 8 && nueva.any { it.isLetter() } && nueva.any { it.isDigit() }

    Surface(color = Colores.Fondo, modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier.systemBarsPadding().imePadding().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text("PRIMER INGRESO", style = MaterialTheme.typography.labelMedium, color = Colores.Tinta3)
            Text("Crea tu contraseña", style = MaterialTheme.typography.headlineMedium, color = Colores.Tinta)
            Text("Mínimo 8 caracteres, con letras y números. Solo tú la conocerás; la oficina no puede verla.",
                style = MaterialTheme.typography.bodyMedium, color = Colores.Tinta3)
            OutlinedTextField(nueva, { nueva = it }, label = { Text("Contraseña nueva") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(), colors = campoTexto(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(otra, { otra = it }, label = { Text("Repite la contraseña") }, singleLine = true,
                visualTransformation = PasswordVisualTransformation(), colors = campoTexto(),
                isError = otra.isNotEmpty() && otra != nueva,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
            error?.let { Aviso(it, Colores.Vencido) }
            BotonGrande(if (cargando) "Guardando…" else "Guardar contraseña", Icons.Filled.Check, habilitado = valida && otra == nueva && !cargando) {
                cargando = true; error = null
                alcance.launch {
                    try {
                        repo.cambiarPassword(nueva); alTerminar()
                    } catch (e: Exception) {
                        error = mensajeDeError(e)
                    } finally {
                        cargando = false
                    }
                }
            }
            TextButton(onClick = alSalir) { Text("Salir", color = Colores.Tinta3) }
        }
    }
}
