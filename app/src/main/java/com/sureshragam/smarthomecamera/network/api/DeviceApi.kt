package com.sureshragam.smarthomecamera.network.api

import com.sureshragam.smarthomecamera.network.dto.HeartbeatRequest
import com.sureshragam.smarthomecamera.network.dto.RegisterDeviceRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceApi {

    @POST("/api/devices/register")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    )

    @POST("/api/devices/heartbeat")
    suspend fun sendHeartbeat(
        @Body request: HeartbeatRequest
    )
}