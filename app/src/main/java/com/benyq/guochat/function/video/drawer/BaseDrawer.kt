package com.benyq.guochat.function.video.drawer

import com.benyq.guochat.function.video.OpenGLTools
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author benyq
 * @time 2021/1/16
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseDrawer {

    private val SIZEOF_FLOAT = 4
    private val COORDS_PER_VERTEX = 2
    protected val TEXTURE_COORD_STRIDE = 2 * SIZEOF_FLOAT
    protected val VERTEXTURE_STRIDE = COORDS_PER_VERTEX * SIZEOF_FLOAT

    //program ID
    protected var mProgram = -1

    // 顶点坐标接收者
    protected var mVertexPosHandle: Int = -1

    // 纹理坐标接收者
    protected var mTexturePosHandle: Int = -1

    // 纹理接收者
    protected var mTextureHandle: Int = -1

    //矩阵变换接收者
    protected var mVertexMatrixHandle: Int = -1

    protected var mTexMatrixHandle: Int = -1

    protected lateinit var mVertexBuffer: FloatBuffer
    protected lateinit var mTextureBuffer: FloatBuffer


    abstract fun getVertexShader(): String
    abstract fun getFragmentShader(): String

    abstract fun getVertexCoors(): FloatArray

    abstract fun getTextureCoors(): FloatArray
    abstract fun getLocations()

    abstract fun release()

    open fun drawFrame(textureId: Int = 0, texMatrix: FloatArray? = null, mvpMatrix: FloatArray? = null) {
        createProgram()
    }

    fun createProgram() {
        if (mProgram > 0) {
            return
        }

        if (mProgram == -1) {
            mProgram = OpenGLTools.createProgram(getVertexShader(), getFragmentShader())
            getLocations()
        }

        val bb = ByteBuffer.allocateDirect(getVertexCoors().size * 4)
        bb.order(ByteOrder.nativeOrder())
        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer.put(getVertexCoors())
        mVertexBuffer.position(0)

        val cc = ByteBuffer.allocateDirect(getTextureCoors().size * 4)
        cc.order(ByteOrder.nativeOrder())
        mTextureBuffer = cc.asFloatBuffer()
        mTextureBuffer.put(getTextureCoors())
        mTextureBuffer.position(0)
    }
}