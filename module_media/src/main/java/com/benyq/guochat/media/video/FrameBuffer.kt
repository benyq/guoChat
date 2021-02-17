package com.benyq.guochat.media.video

import android.opengl.GLES20

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description OpenGL FBO
 */

class FrameBuffer(val width: Int, val height: Int) {

    val texture: Int = OpenGLTools.createFBOTexture(width, height)
    private val fboId: Int = OpenGLTools.createFrameBuffer()

    init {

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture, 0)

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

    }

    fun bind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)
    }

    fun unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }

    fun delete() {
        OpenGLTools.deleteFBO(intArrayOf(fboId), intArrayOf(texture))
    }
}