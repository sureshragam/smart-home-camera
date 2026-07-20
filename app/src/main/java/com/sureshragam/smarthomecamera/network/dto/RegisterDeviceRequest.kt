package com.sureshragam.smarthomecamera.network.dto



data class RegisterDeviceRequest(

    val deviceCode: String,

    val ipAddress: String,

    val firmwareVersion: String
)