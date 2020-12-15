package com.benyq.guochat.study

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.R
import kotlinx.android.synthetic.main.activity_g_l_surface_camera.*

/**
 * @author benyq
 * @time 2020/11/22
 * @e-mail 1520063035@qq.com
 * @note 这个是利用GLSurfaceView预览相机的Activity
 */
class GLSurfaceCameraActivity : AppCompatActivity() {

    private lateinit var mCameraRenderer: CameraRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_g_l_surface_camera)

        glSurfaceView.setEGLContextClientVersion(2)
        mCameraRenderer = CameraRenderer(this, glSurfaceView)
        glSurfaceView.setRenderer(mCameraRenderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onResume() {
        super.onResume()
        mCameraRenderer.onResume()
    }

    override fun onPause() {
        super.onPause()
        mCameraRenderer.onPause()
    }
}