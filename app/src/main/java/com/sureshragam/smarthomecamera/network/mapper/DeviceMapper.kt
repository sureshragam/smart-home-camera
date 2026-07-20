package com.sureshragam.smarthomecamera.network.mapper

import com.sureshragam.smarthomecamera.device.DeviceInfo
import com.sureshragam.smarthomecamera.network.dto.HeartbeatRequest
import com.sureshragam.smarthomecamera.network.dto.RegisterDeviceRequest

fun DeviceInfo.toRegisterRequest(): RegisterDeviceRequest {

    return RegisterDeviceRequest(
        deviceCode = deviceCode,
        ipAddress = localIp,
        firmwareVersion = appVersion
    )
}
fun DeviceInfo.toHeartbeatRequest(): HeartbeatRequest {
    return HeartbeatRequest(
        deviceCode = deviceCode,
        ipAddress = localIp,
        firmwareVersion = appVersion,
        wifiStrength = wifiStrength
    )
}
