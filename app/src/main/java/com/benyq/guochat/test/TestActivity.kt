package com.benyq.guochat.test

import android.graphics.Bitmap
import android.graphics.Matrix
import android.opengl.EGL14
import android.opengl.GLES20
import android.util.Log
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.app.chatImgPath
import com.benyq.guochat.app.chatVideoPath
import com.benyq.guochat.databinding.ActivityTestBinding
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.function.video.OpenGLTools
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.opengles.GL10

class TestActivity : BaseActivity() {

    private val TAG = "TestActivity"

    private val mBinding: ActivityTestBinding by binding()
    private val mCaptureController by lazy { CaptureController(this, mBinding.glSurfaceView) }

    override fun isHideBar() = true

    override fun getLayoutView() = mBinding.root

    override fun initView() {
        lifecycle.addObserver(mCaptureController)
        resizeViewMargin()
    }

    var hasFilter = false
    override fun initListener() {
        mBinding.captureView.setPictureAction {
            takePic = true
        }
        mBinding.captureView.setVideoStartAction {
            startRecording()
        }
        mBinding.captureView.setVideoEndAction {
            stopRecording()
        }

        mBinding.ivCameraChange.setOnClickListener {
            mCaptureController.switchCamera()
        }

        mBinding.btnAddFilter.setOnClickListener {
            if (!hasFilter) {
                mCaptureController.updateFilter(FilterFactory.createFilter(FilterType.FILTER_CARTOON))
            }else {
                mCaptureController.removeFilter()
            }
            hasFilter = !hasFilter
        }

        mCaptureController.setOnDrawFrameListener(object : OnDrawFrameListener {
            override fun onDrawFrame(
                cameraTexId: Int,
                cameraWidth: Int,
                cameraHeight: Int,
                mvpMatrix: FloatArray?,
                texMatrix: FloatArray?,
                timeStamp: Long
            ) {
                sendRecordingData(
                    cameraTexId,
                    OpenGLTools.provideIdentityMatrix(),
                    texMatrix,
                    timeStamp
                )
                saveImg(cameraTexId, cameraHeight, cameraWidth, mvpMatrix, texMatrix)
            }
        })
    }


    private fun resizeViewMargin() {

        if (checkFullScreenPhone()) {
            val topMargin = dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCameraChangeParam =
                mBinding.ivCameraChange.layoutParams as FrameLayout.LayoutParams
            ivCameraChangeParam.topMargin = topMargin
            mBinding.ivCameraChange.layoutParams = ivCameraChangeParam

            val ivCloseParam = mBinding.ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            mBinding.ivClose.layoutParams = ivCloseParam
        }
    }


    private var mVideoOutFile: File? = null
    private var mMuxer: MediaMuxerWrapper? = null
    private var mVideoEncoder: MediaVideoEncoder? = null
    private val mRecordLock = Any()
    private var mRecordBarrier: CountDownLatch? = null
    private var mStartTime = 0L

    @Volatile
    private var mIsRecordStopped = false

    /**
     * 录制封装回调
     */
    private val mMediaEncoderListener: MediaEncoder.MediaEncoderListener = object : MediaEncoder.MediaEncoderListener {
        private var mStartRecordTime: Long = 0
        override fun onPrepared(encoder: MediaEncoder?) {
            if (encoder is MediaVideoEncoder) {
                Log.d(TAG, "onPrepared: tid:" + Thread.currentThread().id)
                mBinding.glSurfaceView.queueEvent(Runnable {
                    if (mIsRecordStopped) {
                        return@Runnable
                    }
                    val videoEncoder: MediaVideoEncoder = encoder as MediaVideoEncoder
                    videoEncoder.setEglContext(EGL14.eglGetCurrentContext())
                    synchronized(mRecordLock) { mVideoEncoder = videoEncoder }
                })
            }
            mStartRecordTime = System.currentTimeMillis()
        }

        override fun onStopped(encoder: MediaEncoder?) {
            mRecordBarrier!!.countDown()
            // Call when MediaVideoEncoder's callback and MediaAudioEncoder's callback both are called.
            if (mRecordBarrier!!.count == 0L) {
                Log.d(TAG, "onStopped: tid:" + Thread.currentThread().id)
                // video time long than 1s
                if (System.currentTimeMillis() - mStartRecordTime <= 1000) {
                    Toasts.show(R.string.save_video_too_short)
                    return
                }
                mStartRecordTime = 0
            }
        }
    }

    /**
     * 开始录制
     */
    private fun startRecording() {
        Log.d(TAG, "startRecording: ")
        try {
            mStartTime = 0
            mRecordBarrier = CountDownLatch(2)
            val videoFileName: String = "guoChat" + "_" + getCurrentDate() + ".mp4"
            mVideoOutFile = File(chatVideoPath() + videoFileName)
            mMuxer = MediaMuxerWrapper(mVideoOutFile!!.absolutePath)

            // for video capturing
            val videoWidth: Int = mCaptureController.mCameraHeight
            val videoHeight: Int = mCaptureController.mCameraWidth
            MediaVideoEncoder(mMuxer, mMediaEncoderListener, 480, 854)
            MediaAudioEncoder(mMuxer, mMediaEncoderListener)
            mMuxer?.prepare()
            mMuxer?.startRecording()
        } catch (e: IOException) {
            Log.e(TAG, "startCapture:", e)
        }
    }

    /**
     * 停止录制
     */
    private fun stopRecording() {
        Log.d(TAG, "stopRecording: ")
        if (mMuxer != null) {
            synchronized(mRecordLock) { mVideoEncoder = null }
            mMuxer?.stopRecording()
            mMuxer = null
        }
    }

    /**
     * 发送录制数据
     *
     * @param texId
     * @param texMatrix
     * @param timeStamp
     */
    private fun sendRecordingData(
        texId: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?,
        timeStamp: Long
    ) {
        synchronized(mRecordLock) {
            if (mVideoEncoder == null) {
                return
            }
            mVideoEncoder!!.frameAvailableSoon(texId, texMatrix, mvpMatrix)
            if (mStartTime == 0L) {
                mStartTime = timeStamp
            }
        }
    }

    private var takePic = false
    private fun saveImg(textureId: Int, width: Int, height: Int, mvpMatrix: FloatArray?, texMatrix: FloatArray?) {

        if (!takePic) {
            return
        }
        val start = System.currentTimeMillis()
        val buffer = ByteBuffer.allocateDirect(width * height * 4)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        GLES20.glFinish()
        GLES20.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer)
        buffer.rewind()
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.copyPixelsFromBuffer(buffer)
        val matrix = Matrix()
        matrix.preScale(1f, -1f)
        val finalBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, false)
        bmp.recycle()
        File(chatImgPath(), getCurrentDate()+".jpg").outputStream().use {
            finalBmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        loge("takepic ${System.currentTimeMillis() - start}")
        takePic = false
    }
}