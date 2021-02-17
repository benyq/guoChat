package com.benyq.guochat.media.video.drawer

import android.opengl.GLES20
import com.benyq.guochat.media.video.OpenGLTools

/**
 * @author benyqYe
 * date 2021/1/22
 * e-mail 1520063035@qq.com
 * description 录制视频时的渲染
 */

class VideoDrawer : BaseDrawer(){


    override fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "uniform mat4 uMatrix;" +
                "uniform mat4 uTexMatrix;" +
                "attribute vec4 aCoordinate;" +
                "varying vec2 vCoordinate;" +
                "void  main() {" +
                "  gl_Position = uMatrix * aPosition;" +
                "   vCoordinate = (uTexMatrix * aCoordinate).xy;" +
                "}"
    }

    override fun getFragmentShader(): String {
        //一定要加换行"\n"，否则会和下一行的precision混在一起，导致编译出错
        return "precision mediump float;" +
                "varying vec2 vCoordinate;" +
                "uniform sampler2D uTexture;" +
                "void main() {" +
                "  gl_FragColor = texture2D(uTexture, vCoordinate);" +
                "}"
    }


    override fun getVertexCoors() = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    override fun getTextureCoors() = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )


    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")

        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mVertexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMatrix")
        OpenGLTools.checkLocation(mVertexMatrixHandle, "uMatrix")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mTexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uTexMatrix")
        OpenGLTools.checkLocation(mTexMatrixHandle, "uTexMatrix")
    }

    override fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandle)
        GLES20.glDisableVertexAttribArray(mTexturePosHandle)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteProgram(mProgram)
    }

    override fun drawFrame(textureId: Int, texMatrix: FloatArray?, mvpMatrix: FloatArray?) {
        super.drawFrame(textureId, texMatrix, mvpMatrix)

        GLES20.glUseProgram(mProgram)
        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        //将激活的纹理单元传递到着色器里面
        GLES20.glUniform1i(mTextureHandle, 0)

        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(mVertexPosHandle)
        GLES20.glEnableVertexAttribArray(mTexturePosHandle)
        GLES20.glUniformMatrix4fv(mVertexMatrixHandle, 1, false, mvpMatrix, 0)
        GLES20.glUniformMatrix4fv(mTexMatrixHandle, 1, false, texMatrix, 0)
        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES20.glVertexAttribPointer(mVertexPosHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)

    }
}