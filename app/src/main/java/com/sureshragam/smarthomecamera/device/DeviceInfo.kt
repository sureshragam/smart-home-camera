package com.sureshragam.smarthomecamera.device

data class DeviceInfo(

    val deviceCode: String,

    val deviceName: String,

    val manufacturer: String,

    val model: String,

    val androidVersion: String,

    val localIp: String,

    val streamPort: Int,

    val appVersion: String,

    val deviceType: String,
    val wifiStrength: Int

)