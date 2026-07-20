package com.sureshragam.smarthomecamera.stream

import android.util.Log
import fi.iki.elonen.NanoHTTPD
import java.io.ByteArrayInputStream

class MjpegServer(port: Int) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        Log.d("MJPEG", "Request -> ${session.uri}")

        return when (session.uri) {

            "/" -> {
                newFixedLengthResponse(
                    Response.Status.OK,
                    "text/plain",
                    "Smart Home Camera Server Running"
                )
            }

            "/snapshot" -> {

                val jpeg = StreamManager.getLatestFrame()

                if (jpeg == null) {

                    newFixedLengthResponse(
                        Response.Status.SERVICE_UNAVAILABLE,
                        "text/plain",
                        "Camera not ready"
                    )

                } else {

                    val response = newFixedLengthResponse(
                        Response.Status.OK,
                        "image/jpeg",
                        ByteArrayInputStream(jpeg),
                        jpeg.size.toLong()
                    )

                    response.addHeader("Cache-Control", "no-cache")
                    response.addHeader("Pragma", "no-cache")

                    return response
                }
            }

            "/stream" -> {

                val streamer = MjpegStreamer()

                streamer.start()

                val response = newChunkedResponse(
                    Response.Status.OK,
                    "multipart/x-mixed-replace; boundary=frame",
                    streamer.inputStream()
                )

                response.addHeader("Cache-Control", "no-cache")
                response.addHeader("Pragma", "no-cache")

                response
            }

            else -> {

                newFixedLengthResponse(
                    Response.Status.NOT_FOUND,
                    "text/plain",
                    "404 Not Found"
                )
            }
        }
    }
}