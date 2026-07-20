package com.sureshragam.smarthomecamera.camera.analysis

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream

object BitmapConverter {

    private const val JPEG_QUALITY = 80

    fun toJpeg(image: ImageProxy): ByteArray {

        val bitmap = Bitmap.createBitmap(
            image.width,
            image.height,
            Bitmap.Config.ARGB_8888
        )

        bitmap.copyPixelsFromBuffer(image.planes[0].buffer)

        val stream = ByteArrayOutputStream()

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            JPEG_QUALITY,
            stream
        )

        bitmap.recycle()

        return stream.toByteArray()
    }
}