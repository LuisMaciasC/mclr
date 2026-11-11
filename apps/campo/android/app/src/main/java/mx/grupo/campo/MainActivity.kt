package mx.grupo.campo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import mx.grupo.campo.ui.CampoNav
import mx.grupo.campo.ui.CampoTema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as CampoApp
        setContent {
            CampoTema {
                CampoNav(app.repo, app.red)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (application as CampoApp).repo.sincronizar()
    }
}
