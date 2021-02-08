package com.benyq.guochat.function.video

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.guochat.function.video.filter.BaseFilter
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 相机采集类，暂时使用Camera1 ，以后可能会支持CameraX，Camera2 滚蛋了 ^_^
 * 这个类控制相机，将采集到的数据发给 CaptureRenderer（如果需要的话）
 */

class CaptureController(
    private val mActivity: Activity,
    private val mGlSurfaceView: GLSurfaceView,
    private var mVideoConfig: VideoConfig = VideoConfig()
) : DefaultLifecycleObserver, Camera.PreviewCallback {
    private val TAG = "CaptureController"

    private var mBackgroundHandler: Handler? = null
    private var mCamera: Camera? = null
    private var mCameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    var mCameraWidth = 1920
    var mCameraHeight = 1080

    private var mViewWidth = 0
    private var mViewHeight = 0

    private var mCaptureRenderer: CaptureRenderer = CaptureRenderer(mActivity)
    private lateinit var mPreviewCallbackBufferArray: ByteArray
    private var mSurfaceTexture: SurfaceTexture? = null

    private var mMvpMatrix: FloatArray = FloatArray(16){0f}
    private val mTexMatrix: FloatArray = floatArrayOf(
        0.0f, -1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 1.0f
    )

    private var mCameraOrientation = 270
    private var mTextureId = 0

    private val lifeCycleSensorManager = LifeCycleSensorManager(mActivity)

    init {
        Matrix.setIdentityM(mMvpMatrix, 0)
        mCameraWidth = mVideoConfig.videoResolution.width
        mCameraHeight = mVideoConfig.videoResolution.height

        mCameraFacing = if (mVideoConfig.frontCamera) Camera.CameraInfo.CAMERA_FACING_FRONT else Camera.CameraInfo.CAMERA_FACING_BACK
        VideoPictureCatcher.init(mActivity)
    }

    private var mOnDrawFrameListener: OnDrawFrameListener? = null

    private val onRendererStatusListener: OnRendererStatusListener =
        object : OnRendererStatusListener {
            override fun onSurfaceCreated() {
                mTextureId = mCaptureRenderer.getTextureId()
                mBackgroundHandler?.post {
                    openCamera()
                    startPreview()
                }
                mCaptureRenderer.setTexMatrix(mTexMatrix)
                mCaptureRenderer.setMVPMatrix(mMvpMatrix)
            }

            override fun onDrawFrame(
                cameraTexId: Int,
                mvpMatrix: FloatArray?,
                texMatrix: FloatArray?,
            ) {
                mGlSurfaceView.requestRender()
                mSurfaceTexture?.updateTexImage()
                mSurfaceTexture?.getTransformMatrix(mTexMatrix)

                mCaptureRenderer.setTexMatrix(mTexMatrix)
                mCaptureRenderer.setMVPMatrix(mMvpMatrix)

                VideoPictureCatcher.onDrawFrame()

                VideoPictureCatcher.takePicture(
                    cameraTexId,
                    mCameraHeight,
                    mCameraWidth,
                    OpenGLTools.provideIdentityMatrix(),
                    OpenGLTools.provideIdentityMatrix(),
                    mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT
                )
                VideoPictureCatcher.sendRecordingData(
                    cameraTexId,
                    OpenGLTools.provideIdentityMatrix(),
                    texMatrix,
                    mSurfaceTexture?.timestamp ?: 0 / 1000000
                )
            }

            override fun onSurfaceChanged(viewWidth: Int, viewHeight: Int) {
                super.onSurfaceChanged(viewWidth, viewHeight)
                mViewHeight = viewHeight
                mViewWidth = viewWidth
                confirmMvpMatrix()
            }

        }

    init {
        mCaptureRenderer.mGLStatusListener = onRendererStatusListener
        mGlSurfaceView.setEGLContextClientVersion(2)
        mGlSurfaceView.setRenderer(mCaptureRenderer)
        mGlSurfaceView.keepScreenOn = true
        mGlSurfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifeCycleSensorManager.onCreate()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startBackgroundThread()
        mBackgroundHandler!!.post {
            openCamera()
            startPreview()
        }
        mGlSurfaceView.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mBackgroundHandler?.post { closeCamera() }
        stopBackgroundHandler()
        val countDownLatch = CountDownLatch(1)
        mGlSurfaceView.queueEvent {
            destroyGlSurface()
            countDownLatch.countDown()
        }
        try {
            countDownLatch.await(500, TimeUnit.MILLISECONDS)
        } catch (e: InterruptedException) {
            // ignored
        }
        mGlSurfaceView.onPause()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        lifeCycleSensorManager.onDestroy()
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {

        mCamera?.addCallbackBuffer(data)
        mGlSurfaceView.requestRender()
    }

    fun switchCamera() {
        mBackgroundHandler?.run {
            mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT - mCameraFacing
            closeCamera()
            openCamera()
            startPreview()
        }
    }

    //这个可以叠加滤镜
    fun updateFilter(filter: BaseFilter?) {
        mGlSurfaceView.queueEvent {
            mCaptureRenderer.updateFilter(filter)
        }
    }

    fun removeFilter() {
        mGlSurfaceView.queueEvent {
            mCaptureRenderer.removeFilter()
        }
    }

    //更换滤镜，同时只有一个生效
    fun switchFilter(filter: BaseFilter?) {
        removeFilter()
        updateFilter(filter)
    }

    fun setOnDrawFrameListener(listener: OnDrawFrameListener) {
        mOnDrawFrameListener = listener
    }

    /**
     * @param path 图片路径
     * @param successListener 图片保存监听
     * @param errorListener 错误
     */
    fun takePicture(
        path: String,
        successListener: TakePictureSuccessListener,
        errorListener: TakePictureErrorListener
    ) {
        VideoPictureCatcher.setCatchParams(path, successListener, errorListener)
    }

    //开始录制
    fun startRecording(videoFileName: String, cameraWidth: Int, cameraHeight: Int, success: VideoRecordSuccessListener,
                       error: VideoRecordErrorListener) {
        VideoPictureCatcher.startRecording(videoFileName, cameraWidth, cameraHeight, success, error)
    }

    fun stopRecording() {
        VideoPictureCatcher.stopRecording()
    }

    private fun startBackgroundThread() {
        val handlerThread = HandlerThread("camera", Process.THREAD_PRIORITY_BACKGROUND)
        handlerThread.start()
        mBackgroundHandler = Handler(handlerThread.looper)
    }

    private fun stopBackgroundHandler() {
        mBackgroundHandler?.looper?.quitSafely()
        mBackgroundHandler = null
    }

    private fun openCamera() {
        if (mCamera != null) {
            return
        }
        try {
            mCamera = Camera.open(mCameraFacing)
            if (mCamera == null) {
                throw RuntimeException("no camera")
            }
            CameraUtils.setCameraDisplayOrientation(mActivity, mCameraFacing, mCamera)
            mCameraOrientation =
                if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT) 270 else 90
            val parameters = mCamera!!.parameters
            CameraUtils.setFocusModes(parameters)
            CameraUtils.chooseFrameRate(parameters, mVideoConfig.frameRate.fps)

            val size = CameraUtils.choosePreviewSize(parameters, mCameraWidth, mCameraHeight)
            mCameraWidth = size[0]
            mCameraHeight = size[1]
            parameters.previewFormat = ImageFormat.NV21
            CameraUtils.setParameters(mCamera, parameters)

            confirmMvpMatrix()

        } catch (e: Exception) {
            Log.e(TAG, "openCamera: $e")
        }
    }

    private fun startPreview() {
        if (mTextureId <= 0 || mCamera == null) {
            Log.e(TAG, "startPreview: mCameraTexId == 0 or mCamera == null")
            return
        }
        try {
            mCamera?.stopPreview()
            mPreviewCallbackBufferArray = ByteArray(
                mCameraHeight * mCameraWidth * ImageFormat.getBitsPerPixel(
                    ImageFormat.NV21
                ) / 8
            )
            mCamera?.setPreviewCallbackWithBuffer(this)
            mCamera?.addCallbackBuffer(mPreviewCallbackBufferArray)

            if (mSurfaceTexture == null) {
                mSurfaceTexture = SurfaceTexture(mTextureId)
            }
            mCamera?.setPreviewTexture(mSurfaceTexture)
            mCamera?.startPreview()

        } catch (e: Exception) {
            Log.e(TAG, "cameraStartPreview: $e")
        }

    }

    private fun closeCamera() {
        try {
            mCamera?.run {
                stopPreview()
                setPreviewTexture(null)
                setPreviewCallbackWithBuffer(null)
                release()
                mCamera = null
            }

        } catch (e: Exception) {
            Log.e(TAG, "releaseCamera: ", e)
        }
    }

    private fun confirmMvpMatrix() {
        if (mViewWidth > 0 && mViewHeight > 0 && mCameraWidth > 0 && mCameraHeight > 0) {
            mMvpMatrix = OpenGLTools.changeMvpMatrixCrop(
                OpenGLTools.provideIdentityMatrix(),
                mViewWidth.toFloat(),
                mViewHeight.toFloat(),
                mCameraHeight.toFloat(),
                mCameraWidth.toFloat()
            )

            mCaptureRenderer.setMVPMatrix(mMvpMatrix)
        }
    }

    private fun destroyGlSurface() {
        Log.d(TAG, "destroyGlSurface: ")
        if (mSurfaceTexture != null) {
            mSurfaceTexture!!.release()
            mSurfaceTexture = null
        }
        mCaptureRenderer.onSurfaceDestroy()
    }

}