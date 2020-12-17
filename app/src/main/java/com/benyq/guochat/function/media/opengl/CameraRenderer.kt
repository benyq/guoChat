package com.benyq.guochat.function.media.opengl

import android.app.Activity
import android.graphics.*
import android.hardware.Camera
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Handler
import android.os.HandlerThread
import android.os.Process
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.guochat.app.IMG_PATH
import com.benyq.guochat.app.chatImgPath
import com.benyq.guochat.function.media.opengl.core.GlUtil
import com.benyq.guochat.function.media.rotate
import com.benyq.guochat.function.media.toByteArray
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.tryCatch
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author benyq
 * @time 2020/11/22
 * @e-mail 1520063035@qq.com
 * @note
 */
class CameraRenderer(private val mActivity: Activity, private val mGlSurfaceView: GLSurfaceView, private val renderListener: OnRendererStatusListener) :
    GLSurfaceView.Renderer,
    Camera.PreviewCallback, DefaultLifecycleObserver {

    private val TAG = "CameraRenderer"

    private var mBackgroundHandler: Handler? = null
    private var mCamera: Camera? = null
    private var mCameraFacing: Int = Camera.CameraInfo.CAMERA_FACING_FRONT

    var mCameraWidth = 1920
    var mCameraHeight = 1080

    private var mViewWidth = 0
    private var mViewHeight = 0

    private lateinit var mMvpMatrix: FloatArray
    private var mCameraTexId = 0
    private lateinit var mPreviewCallbackBufferArray: ByteArray
    private var mSurfaceTexture: SurfaceTexture? = null
    private var mCameraNv21Byte: ByteArray? = null
    private lateinit var mProgramTextureOES: ProgramTextureOES
    private val mTexMatrix: FloatArray = floatArrayOf(
        0.0f, -1.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 1.0f)

    private var onPictureCapturedAction: ((String) -> Unit)? = null
    private var onVideoCapturedAction: ((String, Int) -> Unit)? = null
    private var mCameraOrientation = 270
    private var isTakingPic = false

    private var imgDir: String = mActivity.chatImgPath()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        mProgramTextureOES = ProgramTextureOES()
        mCameraTexId = GlUtil.createTextureObject(GLES11Ext.GL_TEXTURE_EXTERNAL_OES)

        mBackgroundHandler?.post {
            openCamera()
            startPreview()
        }
        renderListener.onSurfaceCreated()
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
        renderListener.onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        if (mSurfaceTexture == null) {
            return
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        mSurfaceTexture?.updateTexImage()
        mSurfaceTexture?.getTransformMatrix(mTexMatrix)
        mProgramTextureOES.drawFrame(mCameraTexId, mTexMatrix, mMvpMatrix)
        renderListener.onDrawFrame(mCameraNv21Byte, mCameraTexId, mCameraWidth, mCameraHeight, mMvpMatrix, mTexMatrix, mSurfaceTexture!!.timestamp)
        mGlSurfaceView.requestRender()
    }


    override fun onResume(@NonNull owner: LifecycleOwner) {
        startBackgroundThread()
        mBackgroundHandler?.post {
            openCamera()
            startPreview()
        }
        mGlSurfaceView.onResume()
    }

    override fun onPause(@NonNull owner: LifecycleOwner) {
        mGlSurfaceView.onPause()
        mBackgroundHandler?.post {
            closeCamera()
        }
        stopBackgroundHandler()
    }

    fun switchCamera() {
        mBackgroundHandler?.run {
            mCameraFacing = Camera.CameraInfo.CAMERA_FACING_FRONT - mCameraFacing
            closeCamera()
            openCamera()
            startPreview()
        }
    }

    fun setPictureCapturedAction(action: (String) -> Unit) {
        onPictureCapturedAction = action
    }

    fun setVideoCapturedAction(action: (String, Int) -> Unit) {
        onVideoCapturedAction = action
    }

    fun takePic() {
        isTakingPic = true
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

        } catch (e: Exception) {
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

    override fun onPreviewFrame(data: ByteArray?, camera: Camera?) {

        if (isTakingPic) {
            loge("onPreviewFrame isTakingPic")
            tryCatch({
                val temp = System.currentTimeMillis()
                val picFile = File("$imgDir${temp}.jpg")
                if (data != null) {
                    val yuvImage = YuvImage(data, ImageFormat.NV21, mCameraWidth, mCameraHeight, null)
                    val bos = ByteArrayOutputStream()
                    yuvImage.compressToJpeg(Rect(0, 0, mCameraWidth, mCameraHeight), 100, bos)
                    val tmpData = bos.toByteArray()
                    val rawBitmap = BitmapFactory.decodeByteArray(tmpData, 0, tmpData.size)
                    val resultBitmap = if (mCameraFacing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                        rawBitmap.rotate(270f, true)  //前置摄像头旋转270°
                    else
                        rawBitmap.rotate(90f)  //后置摄像头旋转90°

                    picFile.sink().buffer().write(resultBitmap.toByteArray()).close()
                    Toasts.show("图片已保存! 耗时：${System.currentTimeMillis() - temp}    路径：  ${picFile.absolutePath}")
                    onPictureCapturedAction?.invoke(picFile.absolutePath)
                }
            }, {
                it.printStackTrace()
            })
            isTakingPic = false
        }

        mCameraNv21Byte = data
        mCamera?.addCallbackBuffer(data)
        mGlSurfaceView.requestRender()
    }

}