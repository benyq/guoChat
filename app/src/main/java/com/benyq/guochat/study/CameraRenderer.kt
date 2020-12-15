package com.benyq.guochat.study

import android.app.Activity
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author benyq
 * @time 2020/11/22
 * @e-mail 1520063035@qq.com
 * @note
 */
class CameraRenderer(private val mActivity: Activity, private val mGlSurfaceView: GLSurfaceView) : GLSurfaceView.Renderer,
    Camera.PreviewCallback {

    private val TAG = "CameraRenderer"

    private var mBackgroundHandler: Handler ? = null
    private var mCamera: Camera? = null
    private var mCameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    private var mCameraWidth = 1280
    private var mCameraHeight = 720

    private var mViewWidth = 0
    private var mViewHeight = 0

    private lateinit var mMvpMatrix : FloatArray
    private var mCameraTexId = 0
    private lateinit var mPreviewCallbackBufferArray: ByteArray
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mCameraNv21Byte: ByteArray? = null
    private lateinit var mProgramTextureOES: ProgramTextureOES
    private val mTexMatrix: FloatArray = floatArrayOf(0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f)


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mProgramTextureOES = ProgramTextureOES()
        mCameraTexId = GlUtil.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)

        mBackgroundHandler?.post {
            openCamera()
            startPreview()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        if (width != mViewWidth || height != mViewHeight) {
            mMvpMatrix = GlUtil.changeMvpMatrixCrop(
                width.toFloat(),
                height.toFloat(),
                mCameraHeight.toFloat(),
                mCameraWidth.toFloat()
            )
        }
        mViewWidth = width
        mViewHeight = height
    }

    override fun onDrawFrame(gl: GL10?) {
        if (mSurfaceTexture == null) {
            return
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        mSurfaceTexture?.updateTexImage()
        mSurfaceTexture?.getTransformMatrix(mTexMatrix)
        mProgramTextureOES.drawFrame(mCameraTexId, mTexMatrix, mMvpMatrix)
        mGlSurfaceView.requestRender()
    }


    fun onResume() {
        startBackgroundThread()
        mBackgroundHandler?.post {
            openCamera()
            startPreview()
        }
        mGlSurfaceView.onResume()
    }

    fun onPause() {
        mGlSurfaceView.onPause()
        mBackgroundHandler?.post {
            closeCamera()
        }
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
            val parameters = mCamera!!.parameters
            CameraUtils.setFocusModes(parameters)
            CameraUtils.chooseFrameRate(parameters)

            val size = CameraUtils.choosePreviewSize(parameters, mCameraWidth, mCameraHeight)
            mCameraWidth = size[0]
            mCameraHeight = size[1]
            parameters.previewFormat = ImageFormat.NV21
            CameraUtils.setParameters(mCamera, parameters)

            mMvpMatrix = GlUtil.changeMvpMatrixCrop(
                mViewWidth.toFloat(),
                mViewHeight.toFloat(),
                mCameraHeight.toFloat(),
                mCameraWidth.toFloat()
            )

        }catch (e: Exception) {
            Log.e(TAG, "openCamera: $e")
        }
    }

    private fun startPreview() {
        if (mCameraTexId <= 0 || mCamera == null) {
            Log.e(TAG, "startPreview: mCameraTexId == 0 or mCamera == null")
            return
        }
        try {
            mCamera?.stopPreview()
            mPreviewCallbackBufferArray = ByteArray(
                mCameraHeight * mCameraWidth * ImageFormat.getBitsPerPixel(
                    ImageFormat.NV16
                ) / 8
            )
            mCamera?.setPreviewCallbackWithBuffer(this)
            mCamera?.addCallbackBuffer(mPreviewCallbackBufferArray)

            if (mSurfaceTexture == null) {
                mSurfaceTexture = SurfaceTexture(mCameraTexId)
            }
            mCamera?.setPreviewTexture(mSurfaceTexture)
            mCamera?.startPreview()

        }catch (e: Exception) {
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

        }catch (e: Exception) {
            Log.e(TAG, "releaseCamera: ", e)
        }
    }

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {
        mCameraNv21Byte = data
        mCamera?.addCallbackBuffer(data)
        mGlSurfaceView.requestRender()
    }
}