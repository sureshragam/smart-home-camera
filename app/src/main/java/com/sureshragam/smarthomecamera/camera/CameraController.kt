package com.sureshragam.smarthomecamera.camera

import android.content.Context
import android.os.Environment
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CameraController {

    fun captureImage(
        context: Context,
        imageCapture: ImageCapture,
        onSuccess: (File) -> Unit,
        onError: (String) -> Unit
    ) {

        val picturesDir = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "SmartHomeCamera"
        )

        if (!picturesDir.exists()) {
            picturesDir.mkdirs()
        }

        val fileName = SimpleDateFormat(
            "yyyyMMdd_HHmmss",
            Locale.getDefault()
        ).format(Date())

        val photoFile = File(
            picturesDir,
            "IMG_$fileName.jpg"
        )

        val outputOptions =
            ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            androidx.core.content.ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {

                override fun onImageSaved(
                    outputFileResults: ImageCapture.OutputFileResults
                ) {
                    onSuccess(photoFile)
                }

                override fun onError(
                    exception: ImageCaptureException
                ) {
                    onError(exception.message ?: "Unknown Error")
                }
            }
        )
    }
}