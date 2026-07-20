package com.sureshragam.smarthomecamera.device

import android.content.Context
import android.os.Build
import com.sureshragam.smarthomecamera.config.AppConfig
import java.net.NetworkInterface
import android.net.wifi.WifiManager

object DeviceManager {

    fun getDeviceInfo(
        context: Context,
        streamPort: Int,
        appVersion: String,
        deviceType: String
    ): DeviceInfo {

        return DeviceInfo(
            deviceCode = AppConfig.DEVICE_CODE,
            deviceName = "${Build.MANUFACTURER} ${Build.MODEL}",
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE ?: "Unknown",
            localIp = getLocalIpAddress(),
            streamPort = streamPort,
            appVersion = appVersion,
            deviceType = deviceType,
            wifiStrength = getWifiStrength(context)
        )
    }

    private fun getLocalIpAddress(): String {

        return try {

            NetworkInterface.getNetworkInterfaces().toList().forEach { network ->

                network.inetAddresses.toList().forEach { address ->

                    if (!address.isLoopbackAddress &&
                        !address.hostAddress.contains(":")
                    ) {
                        return address.hostAddress
                    }
                }
            }

            ""

        } catch (e: Exception) {
            ""
        }
    }
}

private fun getWifiStrength(context: Context): Int {
    return try {
        val wifiManager =
            context.applicationContext
                .getSystemService(Context.WIFI_SERVICE) as WifiManager

        wifiManager.connectionInfo.rssi
    } catch (e: SecurityException) {
        -127 // Unknown / unavailable
    }
}