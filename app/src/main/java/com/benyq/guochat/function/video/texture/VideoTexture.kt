package com.benyq.guochat.function.video.texture

import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.guochat.function.video.VideoFormat

class VideoTexture(format: VideoFormat) : BaseTexture(){

    init {
        textureId = if(format == VideoFormat.TEXTURE_2D) {
            OpenGLTools.createTextureObject(GLES20.GL_TEXTURE_2D)
        }else {
            OpenGLTools.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        }
    }

}