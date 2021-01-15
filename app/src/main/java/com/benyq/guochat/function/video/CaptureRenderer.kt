package com.benyq.guochat.function.video

import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.benyq.guochat.function.media.opengl.OnRendererStatusListener
import com.benyq.guochat.function.video.drawer.OESDrawer
import com.benyq.guochat.function.video.texture.BaseTexture
import com.benyq.guochat.function.video.texture.VideoTexture
import com.benyq.mvvm.ext.loge
import java.util.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 这个是实现GLSurfaceView.Renderer
 */

class CaptureRenderer : GLSurfaceView.Renderer {

    private lateinit var mTexture: BaseTexture
    private lateinit var mDrawer: OESDrawer
    var mGLStatusListener: OnRendererStatusListener? = null



    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 0f)

        mTexture = VideoTexture(VideoFormat.TEXTURE_OES)
        mDrawer = OESDrawer(mTexture)

        mGLStatusListener?.onSurfaceCreated()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        mGLStatusListener?.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        // 清屏，否则会有画面残留
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        mDrawer.draw()
        mGLStatusListener?.onDrawFrame(null, 1, 0, 0, null, null, 0)
    }


    fun onSurfaceDestroy() {
        mDrawer.release()
        mTexture.release()
    }

    fun getTextureId(): Int {
        return if (this::mTexture.isInitialized) mTexture.textureId else 0
    }

    fun setMVPMatrix(matrix: FloatArray) {
        mDrawer.setMVPMatrix(matrix)
    }

    fun setTexMatrix(matrix: FloatArray) {
        mDrawer.setTexMatrix(matrix)
    }
}