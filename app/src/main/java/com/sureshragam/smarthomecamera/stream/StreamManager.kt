package com.sureshragam.smarthomecamera.stream

object StreamManager {

    @Volatile
    private var latestFrame: ByteArray? = null

    @Volatile
    private var frameCounter: Long = 0

    fun updateFrame(frame: ByteArray) {
        latestFrame = frame
        frameCounter++
    }

    fun getLatestFrame(): ByteArray? {
        return latestFrame
    }

    fun getFrameCounter(): Long {
        return frameCounter
    }
    fun hasFrame(): Boolean {
        return latestFrame != null
    }
}