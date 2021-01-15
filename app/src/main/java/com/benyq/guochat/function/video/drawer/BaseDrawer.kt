package com.benyq.guochat.function.video.drawer

import com.benyq.guochat.function.video.filter.BaseFilter
import com.benyq.guochat.function.video.filter.OESFilter
import com.benyq.guochat.function.video.texture.BaseTexture
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * @author benyq
 * @time 2020/12/21
 * @e-mail 1520063035@qq.com
 * @note 渲染的类，主要会包括一些顶点的处理，滤镜的加入等
 * 最终的渲染操作是由 Filter 类执行的
 */
abstract class BaseDrawer() {

    private val SIZEOF_FLOAT = 4
    private val COORDS_PER_VERTEX = 2
    protected val TEXTURE_COORD_STRIDE = 2 * SIZEOF_FLOAT
    protected val VERTEXTURE_STRIDE = COORDS_PER_VERTEX * SIZEOF_FLOAT

    //坐标变换矩阵
    protected var mMatrix: FloatArray? = null

    protected lateinit var mTexture: BaseTexture

    //默认是OESFilter
    protected var mFilter : BaseFilter = OESFilter()

    init {
        val bb = ByteBuffer.allocateDirect(getVertexCoors().size * 4)
        bb.order(ByteOrder.nativeOrder())
        val vertexBuffer = bb.asFloatBuffer()
        vertexBuffer.put(getVertexCoors())
        vertexBuffer.position(0)

        val cc = ByteBuffer.allocateDirect(getTextureCoors().size * 4)
        cc.order(ByteOrder.nativeOrder())
        val textureBuffer = cc.asFloatBuffer()
        textureBuffer.put(getTextureCoors())
        textureBuffer.position(0)

        mFilter.setPositionBuffer(vertexBuffer, textureBuffer)

    }

    abstract fun getVertexCoors(): FloatArray

    abstract fun getTextureCoors(): FloatArray

    abstract fun draw()

    abstract fun release()

    //设置视频的原始宽高
    fun setVideoSize(videoW: Int, videoH: Int) {}

    //设置OpenGL窗口宽高
    fun setWorldSize(worldW: Int, worldH: Int) {}

    //新增调节alpha接口
    fun setAlpha(alpha: Float){}

    fun setMVPMatrix(matrix: FloatArray) {
        mFilter.setMVPMatrix(matrix)
    }

    fun setTexMatrix(matrix: FloatArray) {
        mFilter.setTexMatrix(matrix)
    }

}