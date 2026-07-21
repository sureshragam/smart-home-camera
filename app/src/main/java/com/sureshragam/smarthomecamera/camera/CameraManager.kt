package com.sureshragam.smarthomecamera.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.sureshragam.smarthomecamera.camera.analysis.FrameAnalyzer

class CameraManager(
    private val context: Context
) {

    val settings = CameraSettings()

    val session = CameraSession()

    fun bindCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView
    ) {

        session.lifecycleOwner = lifecycleOwner
        session.previewView = previewView

        if (session.cameraProvider == null) {

            initializeCameraProvider {
                bindUseCases()
            }

        } else {

            bindUseCases()
        }
    }

    private fun initializeCameraProvider(
        onReady: () -> Unit
    ) {

        val future = ProcessCameraProvider.getInstance(context)

        future.addListener({

            session.cameraProvider = future.get()

            onReady()

        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindUseCases() {

        val lifecycleOwner = session.lifecycleOwner ?: return
        val previewView = session.previewView ?: return
        val cameraProvider = session.cameraProvider ?: return

        session.preview = Preview.Builder().build()

        session.preview!!.surfaceProvider = previewView.surfaceProvider

        val cameraSelector =
            when (settings.facing) {
                CameraFacing.FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                CameraFacing.BACK -> CameraSelector.DEFAULT_BACK_CAMERA
            }

        cameraProvider.unbindAll()

        session.imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(context),
            FrameAnalyzer()
        )

        session.camera =
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                session.preview,
                session.imageCapture,
                session.imageAnalysis
            )
    }

    fun switchCamera() {

        settings.facing =
            if (settings.facing == CameraFacing.BACK)
                CameraFacing.FRONT
            else
                CameraFacing.BACK

        bindUseCases()
    }
}