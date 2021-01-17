package com.benyq.guochat.function.video

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.benyq.guochat.R
import com.benyq.guochat.function.media.opengl.OnRendererStatusListener
import com.benyq.guochat.function.video.drawer.BitmapDrawer
import com.benyq.guochat.function.video.drawer.VideoOESDrawer
import com.benyq.guochat.function.video.filter.GrayFilter
import com.benyq.mvvm.ext.loge
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 这个是实现GLSurfaceView.Renderer
 */

class CaptureRenderer(val context: Context) : GLSurfaceView.Renderer {

    private lateinit var mDrawer: VideoOESDrawer
    private lateinit var bitmapDrawer: BitmapDrawer
    private lateinit var mGrayFilter: GrayFilter
    private lateinit var mFrameBuffer: FrameBuffer

    private var mMvpMatrix: FloatArray = FloatArray(16){0f}
    private var mTexMatrix: FloatArray = floatArrayOf(
        0.0f, -1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 1.0f
    )

    var mGLStatusListener: OnRendererStatusListener? = null
    private var mTextureId = -1


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        //开启混合，即半透明
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        mTextureId = OpenGLTools.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)
        mDrawer = VideoOESDrawer()
        mGrayFilter = GrayFilter()
        bitmapDrawer = BitmapDrawer(BitmapFactory.decodeResource(context.resources, R.drawable.ic_app_logo))
        mGLStatusListener?.onSurfaceCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        bitmapDrawer.setViewPoint(width, height)
        mGrayFilter.setTextureSize(mTextureId, width, height)
        mGLStatusListener?.onSurfaceChanged(width, height)
        mFrameBuffer = FrameBuffer(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 清屏，否则会有画面残留
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

//        mGrayFilter.bind()
//        mDrawer.drawFrame(mTextureId, mTexMatrix, mMvpMatrix)
//        mGrayFilter.draw()
//        mGrayFilter.unbind()


        mFrameBuffer.bind()
        mDrawer.drawFrame(mTextureId, mTexMatrix, mMvpMatrix)
        mFrameBuffer.unbind()

        mDrawer.drawFrame(mFrameBuffer.texture, mTexMatrix, mMvpMatrix)

        bitmapDrawer.drawFrame()
        mGLStatusListener?.onDrawFrame(null, 1, 0, 0, null, null, 0)
    }


    fun onSurfaceDestroy() {
        mDrawer.release()
    }

    fun getTextureId() = mTextureId

    fun setMVPMatrix(matrix: FloatArray) {
        mMvpMatrix = matrix
    }

    fun setTexMatrix(matrix: FloatArray) {
        mTexMatrix = matrix
    }
}