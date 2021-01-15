package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.guochat.function.video.OpenGLTools
import java.nio.FloatBuffer

/**
 * @author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 滤镜基类，创建 openGL program
 */

abstract class BaseFilter {

    //纹理ID
    protected var mTextureId: Int = -1

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

    abstract fun getLocations()

    open fun draw(){
        if (mProgram == -1) {
            mProgram = OpenGLTools.createProgram(getVertexShader(), getFragmentShader())
            getLocations()
        }
        GLES20.glUseProgram(mProgram)
    }

    abstract fun getVertexShader(): String
    abstract fun getFragmentShader(): String

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

    fun updateTexture(textureId: Int) {
        mTextureId = textureId
    }
}