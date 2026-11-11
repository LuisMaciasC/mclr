package mx.grupo.campo

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import mx.grupo.campo.data.Api
import mx.grupo.campo.data.CampoDb
import mx.grupo.campo.data.Red
import mx.grupo.campo.data.Repositorio
import mx.grupo.campo.data.Sesion

class CampoApp : Application() {
    lateinit var repo: Repositorio
        private set
    lateinit var red: Red
        private set

    override fun onCreate() {
        super.onCreate()
        WorkManager.initialize(this, Configuration.Builder().build())
        val sesion = Sesion(this)
        repo = Repositorio(this, sesion, CampoDb.de(this).dao(), Api(sesion))
        red = Red(this).also { it.alVolver = { repo.sincronizar() } }
        repo.programarPeriodico()
        repo.sincronizar()
    }
}
