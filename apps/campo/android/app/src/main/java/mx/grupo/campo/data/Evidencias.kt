package mx.grupo.campo.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

/** Fotos y firmas en almacenamiento privado de la app hasta que Odoo confirma que las recibió. */
object Evidencias {
    private const val LADO_MAX = 1600
    private const val CALIDAD = 72

    fun carpeta(ctx: Context): File = File(ctx.filesDir, "evidencias").apply { mkdirs() }

    /** Archivo temporal + Uri para que la cámara del sistema escriba la foto. */
    fun nuevaCaptura(ctx: Context): Pair<File, Uri> {
        val f = File(carpeta(ctx), "captura-${UUID.randomUUID()}.jpg")
        val uri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.archivos", f)
        return f to uri
    }

    /** Reduce a 1600 px, corrige la rotación y comprime a JPEG (~200-400 KB). Borra el original. */
    fun comprimir(ctx: Context, original: File): File? {
        if (!original.exists() || original.length() == 0L) return null
        val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        BitmapFactory.decodeFile(original.path, opts)
        var muestra = 1
        while (opts.outWidth / (muestra * 2) >= LADO_MAX || opts.outHeight / (muestra * 2) >= LADO_MAX) muestra *= 2
        val bmp = BitmapFactory.decodeFile(original.path, BitmapFactory.Options().apply { inSampleSize = muestra }) ?: return null
        val grados = when (ExifInterface(original.path).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }
        val escala = minOf(1f, LADO_MAX.toFloat() / maxOf(bmp.width, bmp.height))
        val m = Matrix().apply { postRotate(grados); postScale(escala, escala) }
        val final = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, m, true)
        val destino = File(carpeta(ctx), "foto-${UUID.randomUUID()}.jpg")
        FileOutputStream(destino).use { final.compress(Bitmap.CompressFormat.JPEG, CALIDAD, it) }
        if (final !== bmp) final.recycle()
        bmp.recycle()
        original.delete()
        return destino
    }

    fun guardarFirma(ctx: Context, bmp: Bitmap): File {
        val destino = File(carpeta(ctx), "firma-${UUID.randomUUID()}.png")
        FileOutputStream(destino).use { bmp.compress(Bitmap.CompressFormat.PNG, 100, it) }
        return destino
    }

    fun borrar(rutas: List<String>) {
        rutas.forEach { File(it).delete() }
    }
}
