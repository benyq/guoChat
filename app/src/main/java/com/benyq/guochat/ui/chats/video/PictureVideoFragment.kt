package com.benyq.guochat.ui.chats.video

import android.graphics.Bitmap
import android.graphics.Matrix
import android.opengl.EGL14
import android.opengl.GLES20
import android.util.Log
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.chatImgPath
import com.benyq.guochat.app.chatVideoPath
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.guochat.function.video.drawer.VideoDrawer
import com.benyq.guochat.function.video.encoder.MediaAudioEncoder
import com.benyq.guochat.function.video.encoder.MediaEncoder
import com.benyq.guochat.function.video.encoder.MediaMuxerWrapper
import com.benyq.guochat.function.video.encoder.MediaVideoEncoder
import com.benyq.guochat.function.video.filter.BaseFilter
import com.benyq.guochat.function.video.filter.FilterFactory
import com.benyq.guochat.function.video.filter.FilterType
import com.benyq.guochat.function.video.listener.OnDrawFrameListener
import com.benyq.guochat.model.bean.VideoFilter
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.ui.base.BaseFragment
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_picture_video.*
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.opengles.GL10

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class PictureVideoFragment : BaseFragment() {

    private val TAG = "PictureVideoFragment"

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()
    private lateinit var mCaptureController: CaptureController
    private val mFilterAdapter by lazy { FilterAdapter() }
    private var mCurrentFilter: BaseFilter ? = null

    override fun getLayoutId() = R.layout.fragment_picture_video

    override fun initView() {
        super.initView()
        mCaptureController = CaptureController(requireActivity(), glSurfaceView)
        lifecycle.addObserver(mCaptureController)

        rvFilters.layoutManager = LinearLayoutManager(mContext)
        rvFilters.adapter = mFilterAdapter
        mFilterAdapter.setNewInstance(FilterType.provideFilters())
        mFilterAdapter.setOnItemClickListener { adapter, view, position ->
            mFilterAdapter.selectFilter(position)

            mCurrentFilter = FilterFactory.createFilter(mFilterAdapter.data[position].type)
            mCaptureController.switchFilter(mCurrentFilter)
        }
        mFilterAdapter.selectFilter(0)
    }

    override fun initListener() {

        ivClose.setOnClickListener {
            pictureVideoViewModel.clearAll()
        }

        ivCameraChange.setOnClickListener {
            mCaptureController.switchCamera()
        }

        with(captureView) {
            setPictureAction {
                takePic = true
            }

            setVideoStartAction {
                startRecording()
            }

            setVideoEndAction {
                stopRecording()
            }
        }
        ivAddFilters.setOnClickListener {
            if (rvFilters.isGone) {
                rvFilters.visible()
            }else {
                rvFilters.gone()
            }
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
                saveImg(cameraTexId, cameraHeight, cameraWidth, OpenGLTools.provideIdentityMatrix(), OpenGLTools.provideIdentityMatrix())
            }
        })
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
                glSurfaceView.queueEvent(Runnable {
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
                val duration = System.currentTimeMillis() - mStartRecordTime
                if (duration <= 1000) {
                    Toasts.show(R.string.save_video_too_short)
                    return
                }
                mStartRecordTime = 0
                Toasts.show(R.string.save_video_success)
                pictureVideoViewModel.showVideoConfirm(mVideoOutFile!!.absolutePath,
                    (duration / 1000).toInt()
                )
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
            val videoFileName: String = requireActivity().chatVideoPath() + "guoChat" + "_" + getCurrentDate() + ".mp4"
            mVideoOutFile = File(videoFileName)
            mMuxer = MediaMuxerWrapper(mVideoOutFile!!.absolutePath)

            // for video capturing
            var cameraWidth: Int = mCaptureController.mCameraHeight
            val cameraHeight: Int = mCaptureController.mCameraWidth

            val videoHeight = cameraHeight
            val videoWidth = (videoHeight.toFloat() / requireActivity().getScreenHeight() * requireActivity().getScreenWidth()).toInt()

            MediaVideoEncoder(mMuxer, mMediaEncoderListener, videoWidth, videoHeight)
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
    private fun saveImg(
        textureId: Int,
        w: Int,
        h: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?
    ) {

        if (!takePic) {
            return
        }
        val screenWidth = requireActivity().getScreenWidth()
        val screenHeight = requireActivity().getScreenHeight()

        val height = h
        val width = (h.toFloat() / screenHeight * screenWidth).toInt()

        val start = System.currentTimeMillis()

        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D,
            0,
            GLES20.GL_RGBA,
            width,
            height,
            0,
            GLES20.GL_RGBA,
            GLES20.GL_UNSIGNED_BYTE,
            null
        )
        val frameBuffers = IntArray(1)
        GLES20.glGenFramebuffers(1, frameBuffers, 0)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0])
        GLES20.glFramebufferTexture2D(
            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D,
            textures[0], 0
        )
        val viewport = IntArray(4)
        GLES20.glGetIntegerv(GLES20.GL_VIEWPORT, viewport, 0)
        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0f, 0f, 0f, 0f)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        val videoDrawer = VideoDrawer()
        android.opengl.Matrix.scaleM(mvpMatrix, 0, 1f,1f,1f)
        videoDrawer.drawFrame(textureId, texMatrix, mvpMatrix)
        videoDrawer.release()

        val buffer = ByteBuffer.allocateDirect(width * height * 4)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        GLES20.glFinish()
        GLES20.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, buffer)
        buffer.rewind()
        GLES20.glViewport(viewport[0], viewport[1], viewport[2], viewport[3])
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, GLES20.GL_NONE)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, GLES20.GL_NONE)
        GLES20.glDeleteTextures(1, textures, 0)
        GLES20.glDeleteFramebuffers(1, frameBuffers, 0)


        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.copyPixelsFromBuffer(buffer)
        val matrix = Matrix()
        matrix.preScale(1f, 1f)
        val finalBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, false)
        bmp.recycle()
        val imgPath = requireActivity().chatImgPath() + getCurrentDate() + ".jpg"
        File(imgPath).outputStream().use {
            finalBmp.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        loge("takepic ${System.currentTimeMillis() - start}")
        Toasts.show(R.string.save_photo_success)
        takePic = false
        pictureVideoViewModel.showPictureConfirm(imgPath)
    }

}