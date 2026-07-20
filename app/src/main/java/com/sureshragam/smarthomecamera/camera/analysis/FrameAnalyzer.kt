package com.sureshragam.smarthomecamera.camera.analysis

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.sureshragam.smarthomecamera.stream.StreamManager
import java.io.ByteArrayOutputStream

class FrameAnalyzer : ImageAnalysis.Analyzer {

    override fun analyze(image: ImageProxy) {

        try {

            StreamManager.updateFrame(
                BitmapConverter.toJpeg(image)
            )

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            image.close()
        }
    }
}