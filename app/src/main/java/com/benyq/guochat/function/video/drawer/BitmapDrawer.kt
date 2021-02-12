package com.benyq.guochat.function.video.drawer

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.Matrix
import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.module_base.ext.loge

class BitmapDrawer(bitmap: Bitmap) : BaseDrawer() {

    private var mViewWidth = 0f
    private var mViewHeight = 0f

    private var mBitmapWidth = 0f
    private var mBitmapHeight = 0f

    private var mMvpMatrix = FloatArray(16){0f}
    private var scale = 1f

    //纹理ID
     var mTextureId: Int = -1

    init {
        mBitmapWidth = bitmap.width.toFloat()
        mBitmapHeight = bitmap.height.toFloat()

        mTextureId = OpenGLTools.createImageTexture(bitmap)
        bitmap.recycle()
        Matrix.setIdentityM(mMvpMatrix, 0)
    }

    override fun getVertexShader(): String {
        return  "attribute vec4 aPosition;" + //顶点坐标
                "attribute vec2 aCoordinate;" +//纹理坐标
                //用于传递纹理坐标给片元着色器，命名和片元着色器中的一致
                "uniform mat4 uMatrix;" +
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "  gl_Position = uMatrix * aPosition;" +
                "  vCoordinate = aCoordinate;" +
                "}"
    }

    override fun getFragmentShader(): String {
        //配置float精度，使用了float数据一定要配置：lowp(低)/mediump(中)/highp(高)
        return "precision mediump float;" +
                //从Java传递进入来的纹理单元
                "uniform sampler2D uTexture;" +
                //从顶点着色器传递进来的纹理坐标
                "varying vec2 vCoordinate;" +
                "void main() {" +
                //根据纹理坐标，从纹理单元中取色
                "  vec4 color = texture2D(uTexture, vCoordinate);" +
                "  gl_FragColor = color;" +
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

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mVertexMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMatrix")

    }

    override fun drawFrame(textureId: Int, texMatrix: FloatArray?, mvpMatrix: FloatArray?) {
        super.drawFrame(textureId, texMatrix, mvpMatrix)

        GLES20.glUseProgram(mProgram)

        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId)
        //将激活的纹理单元传递到着色器里面
        GLES20.glUniform1i(mTextureHandle, 0)

        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(mVertexPosHandle)
        GLES20.glEnableVertexAttribArray(mTexturePosHandle)

        GLES20.glUniformMatrix4fv(mVertexMatrixHandle, 1, false, mMvpMatrix, 0)
        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES20.glVertexAttribPointer(mVertexPosHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)
    }

    override fun release() {
        Matrix.setIdentityM(mMvpMatrix, 0)
        GLES20.glDisableVertexAttribArray(mVertexPosHandle)
        GLES20.glDisableVertexAttribArray(mTexturePosHandle)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteTextures(1, intArrayOf(mTextureId), 0)
        GLES20.glDeleteProgram(mProgram)
    }

    fun setViewPoint(width: Int, height: Int) {
        mViewWidth = width.toFloat()
        mViewHeight = height.toFloat()
        scale = 1f
        Matrix.scaleM(mMvpMatrix, 0, scale * mBitmapWidth / mViewWidth, scale * mBitmapHeight / mViewHeight, 1f)
    }

}