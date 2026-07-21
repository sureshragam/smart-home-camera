package com.sureshragam.smarthomecamera.camera.api

import fi.iki.elonen.NanoHTTPD
import org.json.JSONObject

object CameraCommandController {

    fun handle(session: NanoHTTPD.IHTTPSession): NanoHTTPD.Response {

        return when (session.uri) {

            "/camera/status" -> {
                json(
                    JSONObject()
                        .put("status", "online")
                        .put("camera", "ready")
                        .put("version", "1.0")
                )
            }

            "/camera/capture" -> {
                json(
                    JSONObject()
                        .put("success", false)
                        .put("message", "Not implemented")
                )
            }

            "/camera/switch" -> {
                json(
                    JSONObject()
                        .put("success", false)
                        .put("message", "Not implemented")
                )
            }

            else -> {
                NanoHTTPD.newFixedLengthResponse(
                    NanoHTTPD.Response.Status.NOT_FOUND,
                    "application/json",
                    JSONObject()
                        .put("success", false)
                        .put("message", "Endpoint not found")
                        .toString()
                )
            }
        }
    }

    private fun json(body: JSONObject): NanoHTTPD.Response {

        val response = NanoHTTPD.newFixedLengthResponse(
            NanoHTTPD.Response.Status.OK,
            "application/json",
            body.toString()
        )

        response.addHeader("Cache-Control", "no-cache")

        return response
    }
}