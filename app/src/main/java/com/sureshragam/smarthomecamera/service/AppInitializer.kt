package com.sureshragam.smarthomecamera.service

import android.content.Context
import android.util.Log
import com.sureshragam.smarthomecamera.config.AppConfig
import com.sureshragam.smarthomecamera.device.DeviceManager
import com.sureshragam.smarthomecamera.network.mapper.toRegisterRequest
import com.sureshragam.smarthomecamera.repository.DeviceRepository
import com.sureshragam.smarthomecamera.stream.MjpegServer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppInitializer {

    private var initialized = false

    fun start(context: Context) {

        if (initialized) {
            Log.i("APP", "Already initialized")
            return
        }

        initialized = true

        CoroutineScope(Dispatchers.IO).launch {

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

                Log.i("DEVICE", "Registration successful")

            } catch (e: Exception) {

                Log.e("DEVICE", "Registration failed", e)
            }

            try {

                val server = MjpegServer(AppConfig.STREAM_PORT)
                server.start()

                Log.i("SERVER", "Server started on ${AppConfig.STREAM_PORT}")

            } catch (e: Exception) {

                Log.e("SERVER", "Failed to start server", e)
            }

            try {

                HeartbeatManager(repository).start(context)

                Log.i("HEARTBEAT", "Heartbeat started")

            } catch (e: Exception) {

                Log.e("HEARTBEAT", "Heartbeat failed", e)
            }
        }
    }
}