package com.sureshragam.smarthomecamera.stream

import android.util.Log
import java.io.PipedInputStream
import java.io.PipedOutputStream

class MjpegStreamer {

    private val output = PipedOutputStream()
    private val input = PipedInputStream(output, 1024 * 1024)

    fun inputStream(): PipedInputStream = input

    fun start() {

        Thread {

            while (true) {

                try {

                    val jpeg = StreamManager.getLatestFrame()

                    if (jpeg != null) {

                        output.write("--frame\r\n".toByteArray())

                        output.write(
                            "Content-Type: image/jpeg\r\n".toByteArray()
                        )

                        output.write(
                            "Content-Length: ${jpeg.size}\r\n\r\n".toByteArray()
                        )

                        output.write(jpeg)

                        output.write("\r\n".toByteArray())

                        output.flush()
                    }

                    Thread.sleep(33)

                } catch (e: Exception) {
                    Log.d("MJPEG", "Client disconnected")
                    output.close()
                    break

                }
            }

        }.start()
    }
}