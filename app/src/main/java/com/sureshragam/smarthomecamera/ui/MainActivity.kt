package com.sureshragam.smarthomecamera.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sureshragam.smarthomecamera.camera.CameraScreen
import com.sureshragam.smarthomecamera.service.AppInitializer
import com.sureshragam.smarthomecamera.ui.theme.SmartHomeCameraTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Initialize application once
        AppInitializer.start(this)

        setContent {

            SmartHomeCameraTheme {

                Box(
                    modifier = Modifier.fillMaxSize()
                ) {

                    CameraScreen()

                    DashboardScreen()

                }

            }

        }
    }
}