package com.sureshragam.smarthomecamera.repository

import com.sureshragam.smarthomecamera.network.ApiClient
import com.sureshragam.smarthomecamera.network.api.DeviceApi
import com.sureshragam.smarthomecamera.network.dto.HeartbeatRequest
import com.sureshragam.smarthomecamera.network.dto.RegisterDeviceRequest

class DeviceRepository {

    private val api = ApiClient
        .retrofit
        .create(DeviceApi::class.java)

    suspend fun registerDevice(
        request: RegisterDeviceRequest
    ) {
        api.registerDevice(request)
    }
    suspend fun sendHeartbeat(request: HeartbeatRequest) {
        api.sendHeartbeat(request)
    }
}