package com.benyq.guochat.function.video.filter

import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.Matrix
import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.mvvm.ext.loge
import java.util.*

/**
 * @author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 具体渲染的，画出正常画面
 */

class OESFilter : BaseFilter(){

    // 半透值接收者
    private var mAlphaHandle: Int = -1
    private var mAlpha: Float = 1f


    override fun getLocations() {

        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        OpenGLTools.checkLocation(mVertexPosHandle, "mVertexPosHandle")

        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mVertexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMatrix")
        OpenGLTools.checkLocation(mVertexMatrixHandle, "uMatrix")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mTexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uTexMatrix")
        OpenGLTools.checkLocation(mTexMatrixHandle, "uTexMatrix")

        mAlphaHandle = GLES20.glGetAttribLocation(mProgram, "alpha")

    }

    override fun getVertexShader(): String {
        return "attribute vec4 aPosition;" +
                "uniform mat4 uMatrix;" +
                "uniform mat4 uTexMatrix;" +
                "attribute vec2 aCoordinate;" +
                "varying vec2 vCoordinate;" +
                "attribute float alpha;" +
                "varying float inAlpha;" +
                "void  main() {" +
                "  gl_Position = uMatrix * aPosition;" +
//                "   vCoordinate = (uTexMatrix * aCoordinate).xy;" +
                "   vCoordinate = aCoordinate;" +
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

    override fun draw() {
        super.draw()
        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mTextureId)
        //将激活的纹理单元传递到着色器里面
        GLES20.glUniform1i(mTextureHandle, 0)

        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(mVertexPosHandle)
        GLES20.glEnableVertexAttribArray(mTexturePosHandle)
        GLES20.glUniformMatrix4fv(mVertexMatrixHandle, 1, false, mMVPMatrix, 0)
        GLES20.glUniformMatrix4fv(mTexMatrixHandle, 1, false, mTexMatrix, 0)
        OpenGLTools.checkGlError("glUniformMatrix4fv mTexMatrixHandle")
        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES20.glVertexAttribPointer(mVertexPosHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)
        GLES20.glVertexAttrib1f(mAlphaHandle, mAlpha)

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)

    }

}