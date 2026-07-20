package com.sureshragam.smarthomecamera.stream.client

import java.io.OutputStream

data class ClientConnection(
    val id: Long,
    val outputStream: OutputStream
)