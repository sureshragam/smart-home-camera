package com.sureshragam.smarthomecamera.service

import android.util.Log
import com.sureshragam.smarthomecamera.config.AppConfig
import com.sureshragam.smarthomecamera.device.DeviceManager
import com.sureshragam.smarthomecamera.network.mapper.toHeartbeatRequest
import com.sureshragam.smarthomecamera.repository.DeviceRepository
import kotlinx.coroutines.delay
import android.content.Context

class HeartbeatManager(
    private val repository: DeviceRepository
) {

    suspend fun start(context: Context) {

        delay(AppConfig.HEARTBEAT_INTERVAL)

        while (true) {

            try {
                val deviceInfo = DeviceManager.getDeviceInfo(
                    context = context,
                    streamPort = AppConfig.STREAM_PORT,
                    appVersion = AppConfig.APP_VERSION,
                    deviceType = AppConfig.DEVICE_TYPE
                )

                repository.sendHeartbeat(deviceInfo.toHeartbeatRequest())

                Log.d("HEARTBEAT", "Heartbeat sent")

            } catch (e: Exception) {
                Log.e("HEARTBEAT", "Heartbeat failed", e)
            }

            delay(AppConfig.HEARTBEAT_INTERVAL)
        }
    }
}