package com.sureshragam.smarthomecamera.network.dto

data class HeartbeatRequest(
    val deviceCode: String,
    val ipAddress: String,
    val firmwareVersion: String,
    val wifiStrength: Int
)
