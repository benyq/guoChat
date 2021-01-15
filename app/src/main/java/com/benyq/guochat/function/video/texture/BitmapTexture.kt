package com.benyq.guochat.function.video.texture

import android.graphics.Bitmap
import com.benyq.guochat.function.video.OpenGLTools

class BitmapTexture(bitmap: Bitmap) : BaseTexture(){

    init {
        textureId = OpenGLTools.createImageTexture(bitmap)
        bitmap.recycle()
    }
}