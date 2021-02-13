package com.benyq.guochat.function.video

import android.content.Context
import android.graphics.Bitmap
import android.opengl.EGL14
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import com.benyq.guochat.R
import com.benyq.guochat.function.video.drawer.VideoDrawer
import com.benyq.guochat.function.video.encoder.MediaAudioEncoder
import com.benyq.guochat.function.video.encoder.MediaEncoder
import com.benyq.guochat.function.video.encoder.MediaMuxerWrapper
import com.benyq.guochat.function.video.encoder.MediaVideoEncoder
import com.benyq.module_base.ext.*
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*
import java.util.concurrent.CountDownLatch
import javax.microedition.khronos.opengles.GL10

/**
 * @author benyq
 * @time 2021/2/6
 * @e-mail 1520063035@qq.com
 * @note 视频录制逻辑
 */

/**
 * 参数 1  视频保存本地地址
 * 参数 2  视频长度
 */
typealias VideoRecordSuccessListener = (String, Int) -> Unit
typealias VideoRecordErrorListener = (Throwable) -> Unit

/**
 * 参数 1  图片
 * 参数 2  图片保存本地地址
 */
typealias TakePictureSuccessListener = (Bitmap, String) -> Unit
typealias TakePictureErrorListener = (Throwable) -> Unit

object VideoPictureCatcher {

    private const val TAG = "VideoPictureCatcher"

    internal var screenWidth = 0
    internal var screenHeight = 0
    internal var orientation = 0

    fun init(context: Context) {
        screenWidth = context.getScreenWidth()
        screenHeight = context.getScreenHeight()
    }


    /****************************录制*****************************************/

    private val mEventQueue: MutableList<Runnable> = mutableListOf()
    private var mVideoOutFile: File? = null
    private var mMuxer: MediaMuxerWrapper? = null
    private var mVideoEncoder: MediaVideoEncoder? = null
    private val mRecordLock = Any()
    private var mRecordBarrier: CountDownLatch? = null
    private var mStartTime = 0L

    @Volatile
    private var mIsRecordStopped = false

    private var mVideoRecordSuccessListener: VideoRecordSuccessListener? = null
    private var mVideoRecordErrorListener: VideoRecordErrorListener? = null

    //开始录制时的方向
    private var recordOrientation: Float = 0f

    /**
     * 开始录制
     */
    fun startRecording(
        videoFileName: String,
        cameraWidth: Int,
        cameraHeight: Int,
        success: VideoRecordSuccessListener,
        error: VideoRecordErrorListener
    ) {
        try {
            mVideoRecordSuccessListener = success
            mVideoRecordErrorListener = error
            mStartTime = 0
            mRecordBarrier = CountDownLatch(2)
            mVideoOutFile = File(videoFileName)
            mMuxer = MediaMuxerWrapper(mVideoOutFile!!.absolutePath)


            var videoHeight = cameraHeight
            var videoWidth = (videoHeight.toFloat() / screenHeight * screenWidth).toInt()
            //需要旋转方向
            if (orientation == 180 || orientation == 0) {
                val tmp = videoHeight
                videoHeight = videoWidth
                videoWidth = tmp
            }
            when(orientation) {
                180 -> recordOrientation = 90f
                0 -> recordOrientation = 270f
                else -> recordOrientation = 0f
            }

            loge("startRecording videoWidth $videoWidth    videoHeight $videoHeight")

            MediaVideoEncoder(mMuxer, mMediaEncoderListener, videoWidth, videoHeight)
            MediaAudioEncoder(mMuxer, mMediaEncoderListener)
            mMuxer?.prepare()
            mMuxer?.startRecording()
        } catch (e: IOException) {
            Log.e(TAG, "startCapture:", e)
            mVideoRecordSuccessListener = null
            mVideoRecordErrorListener?.invoke(e)
            mVideoRecordErrorListener = null
        }
    }

    /**
     * 停止录制
     */
    fun stopRecording() {
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
    fun sendRecordingData(
        texId: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?,
        timeStamp: Long
    ) {
        synchronized(mRecordLock) {
            if (mVideoEncoder == null) {
                return
            }
            Matrix.rotateM(mvpMatrix, 0, recordOrientation, 0f, 0f, 1f)
            mVideoEncoder!!.frameAvailableSoon(texId, texMatrix, mvpMatrix)
            if (mStartTime == 0L) {
                mStartTime = timeStamp
            }
        }
    }

    /**
     * 录制封装回调
     */
    private val mMediaEncoderListener: MediaEncoder.MediaEncoderListener = object : MediaEncoder.MediaEncoderListener {
        private var mStartRecordTime: Long = 0
        override fun onPrepared(encoder: MediaEncoder?) {
            if (encoder is MediaVideoEncoder) {
                Log.d(TAG, "onPrepared: tid:" + Thread.currentThread().id)
                mEventQueue.add(Runnable {
                    if (mIsRecordStopped) {
                        return@Runnable
                    }
                    val videoEncoder: MediaVideoEncoder = encoder
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
                    mVideoOutFile?.delete()?.apply {
                        mVideoOutFile = null
                    }
                    return
                }
                mStartRecordTime = 0
                mVideoOutFile?.absolutePath?.run {
                    mVideoRecordSuccessListener?.invoke(this, (duration / 1000).toInt())
                }
            }
        }
    }

    //在gl环境中执行
    @Synchronized
    fun onDrawFrame() {
        // 执行任务队列中的任务
        while (mEventQueue.isNotEmpty()) {
            mEventQueue.removeAt(0).run()
        }
    }


    /****************************拍照*****************************************/

    private var mTakePictureSuccessListener: TakePictureSuccessListener? = null
    private var mTakePictureErrorListener: TakePictureErrorListener? = null
    private var takePicFlag = false
    private var mPicturePath: String = ""


    /**
     * @param path 图片路径
     * @param success 图片保存监听
     * @param error 错误
     */
    fun setCatchParams(
        path: String,
        success: TakePictureSuccessListener,
        error: TakePictureErrorListener
    ) {
        //目前只支持jpeg
        if (!path.contains(".jpeg") && !path.contains(".jpg")) {
            logw("takePicture 目前只支持jpeg")
            return
        }
        takePicFlag = true
        mPicturePath = path
        mTakePictureSuccessListener = success
        mTakePictureErrorListener = error
    }
    fun takePicture(
        textureId: Int,
        w: Int,
        h: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?, isFrontCamera: Boolean
    ) {
        if (!takePicFlag || mPicturePath.isEmpty()) {
            return
        }

        takePicFlag = false

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
        val matrix = android.graphics.Matrix()
        matrix.preScale(1f, 1f)
        //需要旋转方向
        val saveOrientation = when (orientation) {
            0 -> 270
            180 -> 90
            else -> 0
        }
        matrix.postRotate(saveOrientation.toFloat())
        val finalBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, false)
        bmp.recycle()

        tryCatch({
            File(mPicturePath).sink().buffer().write(toByteArray(finalBmp)).close()
        }, {
            mTakePictureErrorListener?.invoke(it)
        }, {
            Log.d(TAG, "takePicture: cost time ${System.currentTimeMillis() - start}ms")
            mTakePictureSuccessListener?.invoke(finalBmp, mPicturePath)
            mTakePictureSuccessListener = null
        })
    }


    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val os = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
        return os.toByteArray()
    }
}