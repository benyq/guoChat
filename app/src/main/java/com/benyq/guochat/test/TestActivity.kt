package com.benyq.guochat.test

import android.graphics.Bitmap
import android.graphics.Matrix
import android.opengl.EGL14
import android.opengl.GLES20
import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.R
import com.benyq.guochat.app.chatImgPath
import com.benyq.guochat.app.chatVideoPath
import com.benyq.guochat.databinding.ActivityTestBinding
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.guochat.function.video.drawer.VideoDrawer
import com.benyq.guochat.function.video.encoder.MediaAudioEncoder
import com.benyq.guochat.function.video.encoder.MediaEncoder
import com.benyq.guochat.function.video.encoder.MediaMuxerWrapper
import com.benyq.guochat.function.video.encoder.MediaVideoEncoder
import com.benyq.guochat.function.video.filter.FilterFactory
import com.benyq.guochat.function.video.filter.FilterType
import com.benyq.guochat.function.video.listener.OnDrawFrameListener
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.opengles.GL10

@AndroidEntryPoint
class TestActivity : BaseActivity() {

    private val TAG = "TestActivity"
    private lateinit var mViewModel: TestViewModel

    private val mBinding: ActivityTestBinding by binding()

    override fun getLayoutView() = mBinding.root

    override fun initView() {
        mViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
        mViewModel.result.observe(this) {
            mBinding.tvResult.text = it
        }

        mViewModel.test()
    }

    override fun initListener() {

    }


}