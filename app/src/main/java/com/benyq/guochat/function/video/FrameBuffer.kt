package com.benyq.guochat.function.video

import android.opengl.GLES20

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description OpenGL FBO
 */

class FrameBuffer(val width: Int, val height: Int) {

    val texture: Int
    private val fboId: Int

    init {
        val fbos = intArrayOf(1)
        GLES20.glGenFramebuffers(1, fbos, 0)
        fboId = fbos[0]

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)

        texture = OpenGLTools.createFBOTexture(width, height)

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, texture, 0)


        val rbo = intArrayOf(1)
        GLES20.glGenRenderbuffers(GLES20.GL_RENDERBUFFER, rbo, 0)
        GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, rbo[0])
        GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_ATTACHMENT, width, height)
        GLES20.glFramebufferRenderbuffer(GLES20.GL_RENDERBUFFER, GLES20.GL_DEPTH_ATTACHMENT, GLES20.GL_RENDERBUFFER, rbo[0])

        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)

    }

    fun bind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboId)
    }

    fun unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
    }
}