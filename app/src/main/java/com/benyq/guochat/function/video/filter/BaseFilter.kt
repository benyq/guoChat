package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.guochat.function.video.FrameBuffer
import com.benyq.guochat.function.video.OpenGLTools
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 滤镜基类，创建 openGL program
 */

abstract class BaseFilter {

    protected var mFrameBuffer: FrameBuffer? = null

    //program ID
    protected var mProgram = -1

    // 顶点坐标接收者
    protected var mVertexPosHandle: Int = -1

    // 纹理坐标接收者
    protected var mTexturePosHandle: Int = -1

    // 纹理接收者
    protected var mTextureHandle: Int = -1

    //坐标变换矩阵
    protected var mMVPMatrix: FloatArray? = null
    //矩阵变换接收者
    protected var mVertexMatrixHandle: Int = -1

    protected var mTexMatrix: FloatArray? = null
    protected var mTexMatrixHandle: Int = -1

    protected var mVertexBuffer: FloatBuffer? = null
    protected var mTextureBuffer: FloatBuffer? = null

    protected var mWidth: Int = 0
    protected var mHeight: Int = 0

    protected var enableFrameBuffer = true

    abstract fun getLocations()

    fun draw(textureId: Int){
        bindFrameBuffer()
        createProgram()
        onDraw(textureId)
        unBindFrameBuffer()
    }

    abstract fun onDraw(textureId: Int)

    open fun getVertexShader(): String {
        return  "attribute vec4 aPosition;" + //顶点坐标
                "attribute vec2 aCoordinate;" +//纹理坐标
                //用于传递纹理坐标给片元着色器，命名和片元着色器中的一致
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "  gl_Position = vec4(aPosition.x, -aPosition.y, aPosition.z, 1);" +
                "  vCoordinate = aCoordinate;" +
                "}"
    }

    open fun getFragmentShader(): String {
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

    open fun getVertexCoors() = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    open fun getTextureCoors() = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0f, 0f,
        1f, 0f
    )

    open fun createProgram() {
        if (mProgram > 0) {
            return
        }

        mProgram = OpenGLTools.createProgram(getVertexShader(), getFragmentShader())
        getLocations()

        val bb = ByteBuffer.allocateDirect(getVertexCoors().size * 4)
        bb.order(ByteOrder.nativeOrder())
        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer?.put(getVertexCoors())
        mVertexBuffer?.position(0)

        val cc = ByteBuffer.allocateDirect(getTextureCoors().size * 4)
        cc.order(ByteOrder.nativeOrder())
        mTextureBuffer = cc.asFloatBuffer()
        mTextureBuffer?.put(getTextureCoors())
        mTextureBuffer?.position(0)
    }

    fun setMVPMatrix(matrix: FloatArray) {
        mMVPMatrix = matrix
    }

    fun setTexMatrix(matrix: FloatArray) {
        mTexMatrix = matrix
    }

    fun setPositionBuffer(vertexBuffer: FloatBuffer, textureBuffer: FloatBuffer) {
        mVertexBuffer = vertexBuffer
        mTextureBuffer = textureBuffer
    }

    fun setSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
    }

    protected fun bindFrameBuffer() {
        if (mFrameBuffer == null && enableFrameBuffer) {
            mFrameBuffer = FrameBuffer(mWidth, mHeight)
        }
        mFrameBuffer?.bind()
    }

    protected fun unBindFrameBuffer() {
        mFrameBuffer?.unbind()
    }

    open fun getTextureId(): Int {
        return mFrameBuffer?.texture ?: -1
    }

    open fun release() {
        mFrameBuffer?.delete()
        GLES20.glDisableVertexAttribArray(mVertexPosHandle)
        GLES20.glDisableVertexAttribArray(mTexturePosHandle)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteProgram(mProgram)
    }

}