package com.sureshragam.smarthomecamera.camera

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.widget.Toast
import android.util.Log
import androidx.camera.core.ImageAnalysis
import com.sureshragam.smarthomecamera.camera.analysis.FrameAnalyzer
import com.sureshragam.smarthomecamera.config.AppConfig
import com.sureshragam.smarthomecamera.device.DeviceManager
import com.sureshragam.smarthomecamera.network.mapper.toRegisterRequest
import com.sureshragam.smarthomecamera.repository.DeviceRepository
import com.sureshragam.smarthomecamera.service.HeartbeatManager
import com.sureshragam.smarthomecamera.stream.MjpegServer

@Composable
fun CameraScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    var cameraFacing by remember {
        mutableStateOf(CameraFacing.BACK)
    }


    val imageAnalysis = remember {

        ImageAnalysis.Builder()
            .setBackpressureStrategy(
                ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
            )
            .setOutputImageFormat(
                ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
            )
            .build()
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }
    LaunchedEffect(Unit) {

        val repository = DeviceRepository()

        val deviceInfo = DeviceManager.getDeviceInfo(
            context = context,
            streamPort = AppConfig.STREAM_PORT,
            appVersion = AppConfig.APP_VERSION,
            deviceType = AppConfig.DEVICE_TYPE
        )

        try {

            repository.registerDevice(
                deviceInfo.toRegisterRequest()
            )

            Log.d("DEVICE", "Registration successful")
            try {
                val server = MjpegServer(AppConfig.STREAM_PORT)
                server.start()

                Log.i("SERVER", "✅ Server started on port ${AppConfig.STREAM_PORT}")

            } catch (e: Exception) {
                Log.e("SERVER", "❌ Failed to start server", e)
            }

            // Start heartbeats only after successful registration
            HeartbeatManager(repository).start(context)

        } catch (e: Exception) {

            Log.e("DEVICE", "Registration failed", e)
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (!hasPermission) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera permission required")
        }
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->

                val previewView = PreviewView(ctx)

                val cameraProviderFuture =
                    ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({

                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build()

                    preview.surfaceProvider = previewView.surfaceProvider

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    cameraProvider.unbindAll()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        FrameAnalyzer()
                    )
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture,
                        imageAnalysis
                    )

                }, ContextCompat.getMainExecutor(ctx))

                previewView
            }
        )

        Button(
            onClick = {

                CameraController.captureImage(
                    context = context,
                    imageCapture = imageCapture,

                    onSuccess = { file ->

                        Toast.makeText(
                            context,
                            "Saved:\n${file.absolutePath}",
                            Toast.LENGTH_LONG
                        ).show()

                    },

                    onError = { error ->

                        Toast.makeText(
                            context,
                            error,
                            Toast.LENGTH_LONG
                        ).show()

                    }
                )

            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Capture")
        }
        Button(
            onClick = {

                cameraFacing =
                    if (cameraFacing == CameraFacing.BACK) {
                        CameraFacing.FRONT
                    } else {
                        CameraFacing.BACK
                    }

            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text("Switch")
        }
    }
}