package com.benyq.guochat.function.video

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.benyq.guochat.R
import com.benyq.guochat.function.video.drawer.BitmapDrawer
import com.benyq.guochat.function.video.drawer.CameraDrawer
import com.benyq.guochat.function.video.filter.BaseFilter
import com.benyq.guochat.function.video.filter.NoFilter
import com.benyq.guochat.function.video.listener.OnRendererStatusListener
import com.benyq.mvvm.ext.loge
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 这个是实现GLSurfaceView.Renderer
 * 现在每个滤镜还是单独创建一个FrameBuffer的，以后也许会改吧
 */

class CaptureRenderer(val context: Context) : GLSurfaceView.Renderer {

    private lateinit var mDrawer: CameraDrawer
    private lateinit var bitmapDrawer: BitmapDrawer
    private lateinit var mFrameBuffer: FrameBuffer
    //将图像显示到屏幕的滤镜
    private lateinit var mShowFilter: NoFilter
    private val mFilterList : MutableList<BaseFilter> = mutableListOf()


    private var mMvpMatrix: FloatArray = FloatArray(16){0f}
    private var mTexMatrix: FloatArray = floatArrayOf(
        0.0f, -1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 1.0f
    )

    var mGLStatusListener: OnRendererStatusListener? = null
    private var mCameraTextureId = -1
    private var mFilterTextureId = -1
    private var mViewWidth: Int = 0
    private var mViewHeight: Int = 0

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        //开启混合，即半透明
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        mCameraTextureId = OpenGLTools.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        mDrawer = CameraDrawer()
        mShowFilter = NoFilter()

        bitmapDrawer = BitmapDrawer(BitmapFactory.decodeResource(context.resources, R.drawable.ic_app_logo))
        mGLStatusListener?.onSurfaceCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mViewWidth = width
        mViewHeight = height
        bitmapDrawer.setViewPoint(width, height)
        mGLStatusListener?.onSurfaceChanged(width, height)
        mFrameBuffer = FrameBuffer(width, height)

    }


    /**
     * 滤镜处理逻辑
     * 先利用FBO将 相机采集到的OES格式的纹理转换成2D
     * 然后利用FBO继续进行一系列滤镜操作，最后将得到的纹理渲染到屏幕上
     */
    override fun onDrawFrame(gl: GL10?) {
        // 清屏，否则会有画面残留
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        val start = System.currentTimeMillis()
        mFrameBuffer.bind()//FBO 之前出错是因为 纹理格式不一致导致的
        mDrawer.drawFrame(mCameraTextureId, mTexMatrix, mMvpMatrix)
        mFrameBuffer.unbind()
        mFilterTextureId = mFrameBuffer.texture

        mFilterList.forEach {
            it.draw(mFilterTextureId)
            mFilterTextureId = it.getTextureId()
        }

        mShowFilter.draw(mFilterTextureId)
        bitmapDrawer.drawFrame()

        mGLStatusListener?.onDrawFrame(mFilterTextureId, mMvpMatrix, mTexMatrix)
    }


    fun onSurfaceDestroy() {
        GLES20.glDeleteTextures(1, intArrayOf(mCameraTextureId), 0)
        mDrawer.release()
    }

    fun getTextureId() = mCameraTextureId

    fun setMVPMatrix(matrix: FloatArray) {
        mMvpMatrix = matrix
    }

    fun setTexMatrix(matrix: FloatArray) {
        mTexMatrix = matrix
    }

    fun updateFilter(filter: BaseFilter?) {
        filter?.let {
            it.setSize(mViewWidth, mViewHeight)
            mFilterList.add(it)
        }
    }

    fun removeFilter() {
        mFilterList.forEach {
            it.release()
        }
        mFilterList.clear()
    }
}