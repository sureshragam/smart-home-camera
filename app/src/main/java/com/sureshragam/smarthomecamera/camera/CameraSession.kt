package com.sureshragam.smarthomecamera.camera

import androidx.camera.core.Camera
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

class CameraSession {

    var lifecycleOwner: LifecycleOwner? = null

    var cameraProvider: ProcessCameraProvider? = null

    var camera: Camera? = null

    var preview: Preview? = null

    var previewView: PreviewView? = null

    var imageCapture: ImageCapture =
        ImageCapture.Builder().build()

    var imageAnalysis: ImageAnalysis =
        ImageAnalysis.Builder()
            .setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            )
            .setOutputImageFormat(
                ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
            )
            .build()
}