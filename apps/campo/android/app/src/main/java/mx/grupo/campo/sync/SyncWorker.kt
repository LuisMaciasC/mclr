package mx.grupo.campo.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import mx.grupo.campo.CampoApp
import mx.grupo.campo.data.Repositorio

/** Envía la cola de acciones cuando hay red. WorkManager lo reintenta con espera creciente (30 s, 1, 2, 4 min...). */
class SyncWorker(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val repo = (applicationContext as CampoApp).repo
        return when (repo.enviarPendientes()) {
            Repositorio.ResultadoEnvio.Listo -> Result.success()
            Repositorio.ResultadoEnvio.SinSesion -> Result.success() // se reanuda al volver a iniciar sesión
            Repositorio.ResultadoEnvio.Reintentar -> if (runAttemptCount < 50) Result.retry() else Result.failure()
        }
    }
}
