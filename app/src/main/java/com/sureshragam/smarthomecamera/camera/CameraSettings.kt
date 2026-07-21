package com.sureshragam.smarthomecamera.camera

data class CameraSettings(

    var facing: CameraFacing = CameraFacing.FRONT,

    var jpegQuality: Int = 80,

    var frameRate: Int = 30,

    var enablePreview: Boolean = true

)