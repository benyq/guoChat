package com.benyq.guochat.ui.chats.video

import android.opengl.EGL14
import android.opengl.GLSurfaceView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.app.chatVideoPath
import com.benyq.guochat.function.media.encoder.MediaAudioEncoder
import com.benyq.guochat.function.media.encoder.MediaEncoder
import com.benyq.guochat.function.media.encoder.MediaMuxerWrapper
import com.benyq.guochat.function.media.encoder.MediaVideoEncoder
import com.benyq.guochat.function.media.opengl.CameraRenderer
import com.benyq.guochat.function.media.opengl.OnRendererStatusListener
import com.benyq.guochat.function.media.opengl.core.GlUtil
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_picture_video.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.concurrent.CountDownLatch

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class PictureVideoFragment : BaseFragment() {

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()
    private lateinit var mCameraRenderer: CameraRenderer

    @Volatile
    private var mStartTime: Long = 0

    override fun getLayoutId() = R.layout.fragment_picture_video

    override fun initView() {
        super.initView()

        glSurfaceView.setEGLContextClientVersion(2)
        mCameraRenderer = CameraRenderer(
            requireActivity(),
            glSurfaceView,
            object : OnRendererStatusListener {
                override fun onDrawFrame(
                    cameraNv21Byte: ByteArray?,
                    cameraTexId: Int,
                    cameraWidth: Int,
                    cameraHeight: Int,
                    mvpMatrix: FloatArray?,
                    texMatrix: FloatArray?,
                    timeStamp: Long
                ) {
                    sendRecordingData(cameraTexId, GlUtil.IDENTITY_MATRIX, texMatrix, timeStamp / 1000000)
                }
            }).apply {
            setPictureCapturedAction {
                pictureVideoViewModel.showPictureConfirm(it)
            }
        }
        glSurfaceView.setRenderer(mCameraRenderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY

        lifecycle.addObserver(mCameraRenderer)

    }

    override fun initListener() {

        ivClose.setOnClickListener {
            pictureVideoViewModel.clearAll()
        }

        ivCameraChange.setOnClickListener {
            mCameraRenderer.switchCamera()
        }

        with(captureView) {
            setPictureAction {
                mCameraRenderer.takePic()
            }

            setVideoStartAction {
                lifecycleScope.launch(Dispatchers.IO) {
                    startRecording()
                }
            }

            setVideoEndAction {
                lifecycleScope.launch(Dispatchers.IO) {
                    stopRecording()
                }
            }
        }
    }

    private var mVideoOutFile: File? = null
    private var mMuxer: MediaMuxerWrapper? = null
    private var mVideoEncoder: MediaVideoEncoder? = null
    private val mRecordLock = Any()
    private var mRecordBarrier: CountDownLatch? = null

    private val mIsRecordStopped = false

    /**
     * 录制封装回调
     */
    private val mMediaEncoderListener: MediaEncoder.MediaEncoderListener =
        object : MediaEncoder.MediaEncoderListener {
            private var mStartRecordTime: Long = 0
            override fun onPrepared(encoder: MediaEncoder) {
                if (encoder is MediaVideoEncoder) {
                    logd("onPrepared: tid:" + Thread.currentThread().id)
                    glSurfaceView.queueEvent(Runnable {
                        if (mIsRecordStopped) {
                            return@Runnable
                        }
                        val videoEncoder: MediaVideoEncoder = encoder as MediaVideoEncoder
                        videoEncoder.setEglContext(EGL14.eglGetCurrentContext())
                        synchronized(mRecordLock) { mVideoEncoder = videoEncoder }
                    })
//                runOnUiThread(Runnable { mTakePicBtn.setSecond(0) })
                }
                mStartRecordTime = System.currentTimeMillis()
            }

            override fun onStopped(encoder: MediaEncoder?) {
                mRecordBarrier!!.countDown()
                // Call when MediaVideoEncoder's callback and MediaAudioEncoder's callback both are called.
                if (mRecordBarrier!!.count == 0L) {
                    logd("onStopped: tid:" + Thread.currentThread().id)
//                runOnUiThread(Runnable { mTakePicBtn.setSecond(0) })
                    if (System.currentTimeMillis() - mStartRecordTime <= 1000) {
                        runOnUiThread {
                            Toasts.show(R.string.save_video_too_short)
                        }
                        return
                    }
                    mStartRecordTime = 0
                    lifecycleScope.launch {
                        tryCatch({
                            val dcimFile: File =
                                File(requireActivity().chatVideoPath() + mVideoOutFile!!.name)

                        }, {

                        })
                    }
                }
            }
        }

    /**
     * 开始录制
     * 视频录制是采用硬编码的方式，
     * 单是出现opengl error，代码是copy的，所以我不知道怎么改，o(￣┰￣*)ゞ
     * 不过我准备去学习音视频了 https://www.jianshu.com/p/1749d2d43ecb
     * 希望到时候学成归来，能改掉这个bug吧
     */
    private fun startRecording() {
        logd("startRecording: ")
        try {
            mStartTime = 0
            mRecordBarrier = CountDownLatch(1)
            val videoFileName: String = "guoChat" + "_" + getCurrentDate() + ".mp4"
            mVideoOutFile = File(requireActivity().chatVideoPath() + videoFileName)
            loge("mVideoOutFile ${mVideoOutFile!!.absolutePath}")
            mMuxer = MediaMuxerWrapper(mVideoOutFile!!.absolutePath)

            // for video capturing
            val videoWidth: Int = mCameraRenderer.mCameraHeight
            val videoHeight: Int = mCameraRenderer.mCameraWidth
            MediaVideoEncoder(mMuxer, mMediaEncoderListener, videoWidth, videoHeight)
            MediaAudioEncoder(mMuxer, mMediaEncoderListener)
            mMuxer?.prepare()
            mMuxer?.startRecording()
        } catch (e: IOException) {
            logd("startCapture: ${e.message}")
        }
    }

    /**
     * 停止录制
     */
    private fun stopRecording() {
        logd("stopRecording: ")
        mMuxer?.let {
            synchronized(mRecordLock) { mVideoEncoder = null }
            it.stopRecording()
            mMuxer = null
        }
//        pictureVideoViewModel.showVideoConfirm(videoPath, videoDuration = duration)
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

}