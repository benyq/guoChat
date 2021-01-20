package com.benyq.guochat.function.video.drawer

import android.media.effect.EffectFactory
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.benyq.guochat.function.video.OpenGLTools

/**
 * @author benyq
 * @time 2021/1/16
 * @e-mail 1520063035@qq.com
 * @note 主要用于渲染相机数据
 */
class CameraDrawer : BaseDrawer(){

    //透明句柄
    private var mAlphaHandle = -1
    private var mAlpha = 1f

    override fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "uniform mat4 uMatrix;" +
                "uniform mat4 uTexMatrix;" +
                "attribute vec4 aCoordinate;" +
                "varying vec2 vCoordinate;" +
                "attribute float alpha;" +
                "varying float inAlpha;" +
                "void  main() {" +
                "  gl_Position = uMatrix * aPosition;" +
                "   vCoordinate = (uTexMatrix * aCoordinate).xy;" +
                "    inAlpha = alpha;" +
                "}"
    }

    override fun getFragmentShader(): String {
        //一定要加换行"\n"，否则会和下一行的precision混在一起，导致编译出错
        return "#extension GL_OES_EGL_image_external : require\n" +
                "precision mediump float;" +
                "varying vec2 vCoordinate;" +
                "uniform samplerExternalOES uTexture;" +
                "varying float inAlpha;" +
                "void main() {" +
                "  vec4 color = texture2D(uTexture, vCoordinate);" +
                "  gl_FragColor = vec4(color.r, color.g, color.b, inAlpha);" +
                "}"
    }


    override fun getVertexCoors() = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    override fun getTextureCoors() = floatArrayOf(
        0f, 0f,
        1f, 0f,
        0f, 1f,
        1f, 1f
    )

    override fun getLocations() {

        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")

        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mVertexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMatrix")
        OpenGLTools.checkLocation(mVertexMatrixHandle, "uMatrix")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mTexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uTexMatrix")
        OpenGLTools.checkLocation(mTexMatrixHandle, "uTexMatrix")

        mAlphaHandle = GLES20.glGetAttribLocation(mProgram, "alpha")
    }

    override fun drawFrame(textureId: Int, texMatrix: FloatArray?, mvpMatrix: FloatArray?) {
        super.drawFrame(textureId, texMatrix, mvpMatrix)

        GLES20.glUseProgram(mProgram)
        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId)
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
        GLES20.glVertexAttrib1f(mAlphaHandle, mAlpha)

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)

    }

    override fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandle)
        GLES20.glDisableVertexAttribArray(mTexturePosHandle)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteProgram(mProgram)
    }

}