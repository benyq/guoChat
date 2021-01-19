package com.benyq.guochat.function.video.drawer

import android.opengl.GLES20
import com.benyq.guochat.function.video.OpenGLTools
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author benyq
 * @time 2020/12/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class TriangleDrawer{

    private val mVertexCoors = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        0f, 1f
    )

    // 纹理坐标
    private val mTextureCoors = floatArrayOf(
        0f, 1f,
        1f, 1f,
        0.5f, 0f
    )


    private val mColor = floatArrayOf(1f, 0f, 0f, 1f)

    //纹理ID
    private var mTextureId: Int = -1
    //program ID
    private var mProgram = -1


    // 顶点坐标接收者
    private var mVertexPosHandle: Int = -1
    // 纹理坐标接收者
    private var mTexturePosHandle: Int = -1

    private var mColorHandle: Int = -1

    private lateinit var mVertexBuffer: FloatBuffer
    private lateinit var mTextureBuffer: FloatBuffer
    private lateinit var mColorBuffer: FloatBuffer

    init {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0) //生成纹理
        mTextureId = texture[0]
        iniPos()
    }

     fun draw() {
        if (mTextureId != -1) {
            createGLPrg()
            doDraw()
        }
    }


     fun release() {
        GLES20.glDisableVertexAttribArray(mVertexPosHandle)
        GLES20.glDisableVertexAttribArray(mTexturePosHandle)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        GLES20.glDeleteTextures(1, intArrayOf(mTextureId), 0)
        GLES20.glDeleteProgram(mProgram)
    }

    private fun iniPos() {
        val bb = ByteBuffer.allocateDirect(mVertexCoors.size * 4)
        bb.order(ByteOrder.nativeOrder())
        mVertexBuffer = bb.asFloatBuffer()
        mVertexBuffer.put(mVertexCoors)
        mVertexBuffer.position(0)

        val cc = ByteBuffer.allocateDirect(mTextureCoors.size * 4)
        cc.order(ByteOrder.nativeOrder())
        mTextureBuffer = cc.asFloatBuffer()
        mTextureBuffer.put(mTextureCoors)
        mTextureBuffer.position(0)

        val color = ByteBuffer.allocateDirect(mColor.size * 4)
        color.order(ByteOrder.nativeOrder())
        mColorBuffer = color.asFloatBuffer()
        mColorBuffer.put(mColor)
        mColorBuffer.position(0)
    }

    private fun createGLPrg() {
        if (mProgram == -1) {
            val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, getVertexShader())
            val fragmentShader  = loadShader(GLES20.GL_FRAGMENT_SHADER, getFragmentShader())

            mProgram = GLES20.glCreateProgram()
            GLES20.glAttachShader(mProgram, vertexShader)
            GLES20.glAttachShader(mProgram, fragmentShader)
            GLES20.glLinkProgram(mProgram)

            mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
            mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")
            mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor")
        }
        GLES20.glUseProgram(mProgram)
    }

    private fun loadShader(type: Int, shaderCode: String) : Int{
        //根据type创建顶点着色器或者片元着色器
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
        return shader
    }

    private fun doDraw() {
        GLES20.glEnableVertexAttribArray(mVertexPosHandle)
        GLES20.glEnableVertexAttribArray(mTexturePosHandle)
        GLES20.glEnableVertexAttribArray(mColorHandle)

        GLES20.glVertexAttribPointer(mVertexPosHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)
        GLES20.glUniform4fv(mColorHandle, 1, mColorBuffer)
//        OpenGLTools.checkGlError("glUniform4fv")

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 3)
    }

    private fun getVertexShader(): String {
        return "attribute vec4 aPosition;"+
                "void  main() {"+
                "  gl_Position = aPosition;" +
                "}"
    }

    private fun getFragmentShader(): String {
        return "precision mediump float;" +
                "uniform vec4 uColor;"+
                "void main() {" +
                "  gl_FragColor = uColor;" +
                "}"
    }


}