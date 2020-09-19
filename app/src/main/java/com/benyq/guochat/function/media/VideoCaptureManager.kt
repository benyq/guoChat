package com.benyq.guochat.function.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.media.MediaRecorder
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.guochat.app.IMG_PATH
import com.benyq.guochat.app.VIDEO_PATH
import com.benyq.guochat.function.other.DateFormatUtil
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.tryCatch
import java.io.IOException
import java.util.*


/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class VideoCaptureManager(private val context: Activity, private val mTextureView: TextureView) :
    DefaultLifecycleObserver {

    companion object {
        const val PREVIEW_WIDTH = 720                                         //预览的宽度
        const val PREVIEW_HEIGHT = 1280                                       //预览的高度
        const val SAVE_WIDTH = 720                                            //保存图片的宽度
        const val SAVE_HEIGHT = 1280                                          //保存图片的高度
    }

    private val DEFAULT_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 90)
        append(Surface.ROTATION_90, 0)
        append(Surface.ROTATION_180, 270)
        append(Surface.ROTATION_270, 180)
    }

    private val INVERSE_ORIENTATIONS = SparseIntArray().apply {
        append(Surface.ROTATION_0, 270)
        append(Surface.ROTATION_90, 180)
        append(Surface.ROTATION_180, 90)
        append(Surface.ROTATION_270, 0)
    }

    private val SENSOR_ORIENTATION_DEFAULT_DEGREES = 90
    private val SENSOR_ORIENTATION_INVERSE_DEGREES = 270

    private lateinit var mCameraManager: CameraManager
    private var mImageReader: ImageReader? = null
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mPreviewRequest: CaptureRequest? = null
    private lateinit var captureRequestBuilder: CaptureRequest.Builder

    private var mCameraId = "0"
    private lateinit var mCameraCharacteristics: CameraCharacteristics

    private var mCameraSensorOrientation = 0                                            //摄像头方向
    private var mCameraFacing = CameraCharacteristics.LENS_FACING_BACK            //默认使用后置摄像头
    private val mDisplayRotation = context.windowManager.defaultDisplay.rotation  //手机方向

    private var canTakePic = true                                                       //是否可以拍照
    private var canExchangeCamera = false                                               //是否可以切换摄像头

    private var isTakingPic = false

    private var mCameraHandler: Handler
    private val handlerThread = HandlerThread("CameraThread")

    private var mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)                      //预览大小
    private var mSavePicSize = Size(SAVE_WIDTH, SAVE_HEIGHT)                            //保存图片大小

    private var imgDir = context.getExternalFilesDir(IMG_PATH)!!.absolutePath

    private var onPictureCapturedAction : ((String)->Unit) ?= null
    private var onVideoCapturedAction : ((String, Int)->Unit) ?= null

    private var mediaRecorder: MediaRecorder? = null

    private var videoRecordStartTime: Long = 0L

    init {
        handlerThread.start()
        mCameraHandler = Handler(handlerThread.looper)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        mTextureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                initCameraInfo()
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        releaseCamera()
        releaseThread()
        mediaRecorder?.release()
    }


    /**
     * 拍照
     */
    fun takePic() {
        if (mCameraDevice == null || !mTextureView.isAvailable || !canTakePic) return

        mCameraDevice?.apply {

            val captureRequestBuilder = createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            captureRequestBuilder.addTarget(mImageReader!!.surface)

            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)     // 闪光灯
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mCameraSensorOrientation)      //根据摄像头方向对保存的照片进行旋转，使其为"自然方向"
            //停止预览
            mCameraCaptureSession?.let {
                isTakingPic = true
                it.stopRepeating()
                it.capture(captureRequestBuilder.build(), null, mCameraHandler)
            } ?: Toasts.show("拍照异常！")

        }
    }

    /**
     * 切换摄像头
     */
    fun exchangeCamera() {
        if (mCameraDevice == null || !canExchangeCamera || !mTextureView.isAvailable) return

        mCameraFacing = if (mCameraFacing == CameraCharacteristics.LENS_FACING_FRONT)
            CameraCharacteristics.LENS_FACING_BACK
        else
            CameraCharacteristics.LENS_FACING_FRONT

        mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT) //重置预览大小
        releaseCamera()
        initCameraInfo()
    }

    fun restartPreview() {
        //重启预览
        startPreviewSession(mCameraDevice!!)
        loge("重启预览")
    }


    fun setPictureCapturedAction(action: (String) -> Unit) {
        onPictureCapturedAction = action
    }

    fun setVideoCapturedAction(action: (String, Int) -> Unit) {
        onVideoCapturedAction = action
    }

    /**
     * 初始化
     */
    private fun initCameraInfo() {
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIdList = mCameraManager.cameraIdList
        if (cameraIdList.isEmpty()) {
            Toasts.show("没有可用相机")
            return
        }

        for (id in cameraIdList) {
            val cameraCharacteristics = mCameraManager.getCameraCharacteristics(id)
            val facing = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)

            if (facing == mCameraFacing) {
                mCameraId = id
                mCameraCharacteristics = cameraCharacteristics
                loge("选中的摄像头 $id")
            }
            loge("设备中的摄像头 $id")
        }

        val supportLevel = mCameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
        }

        //获取摄像头方向
        mCameraSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0
        //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
        val configurationMap = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)

        val savePicSize = configurationMap!!.getOutputSizes(ImageFormat.JPEG)          //保存照片尺寸
        val previewSize = configurationMap.getOutputSizes(SurfaceTexture::class.java) //预览尺寸

        val exchange = exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        //这个选择mSavePicSize 时出现bug，导致尺寸不正确，使得切换摄像头时MediaRecorder的setVideoSize 导致Surface was Abandoned
        //最后经过排查，是因为切换摄像头时 mSavePicSize为恢复初始数值
        mSavePicSize = Size(SAVE_WIDTH, SAVE_HEIGHT)
        mPreviewSize = Size(PREVIEW_WIDTH, PREVIEW_HEIGHT)

        mSavePicSize = getBestSize(
            if (exchange) mSavePicSize.height else mSavePicSize.width,
            if (exchange) mSavePicSize.width else mSavePicSize.height,
            if (exchange) mTextureView.height else mTextureView.width,
            if (exchange) mTextureView.width else mTextureView.height,
            savePicSize.toList())


        mPreviewSize = getBestSize(
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            if (exchange) mTextureView.height else mTextureView.width,
            if (exchange) mTextureView.width else mTextureView.height,
            previewSize.toList())

        loge("configurationMap ${savePicSize.toList()}")


        mTextureView.surfaceTexture.setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)

        loge("预览最优尺寸 ：${mPreviewSize.width} * ${mPreviewSize.height}, 比例  ${mPreviewSize.width.toFloat() / mPreviewSize.height}")
        loge("保存图片最优尺寸 ：${mSavePicSize.width} * ${mSavePicSize.height}, 比例  ${mSavePicSize.width.toFloat() / mSavePicSize.height}")


        mImageReader = ImageReader.newInstance(mSavePicSize.width, mSavePicSize.height, ImageFormat.JPEG, 1)
        mImageReader?.setOnImageAvailableListener(onImageAvailableListener, mCameraHandler)

        openCamera()
    }

    private val onImageAvailableListener = ImageReader.OnImageAvailableListener {

        val image = it.acquireNextImage()
        image?.run {
            if (isTakingPic) {
                val byteBuffer = image.planes[0].buffer
                val byteArray = ByteArray(byteBuffer.remaining())
                byteBuffer.get(byteArray)
                val imgName = DateFormatUtil.dateToString("yyyyMMdd_HHmmss", Date())
                BitmapUtils.savePic(byteArray, mCameraSensorOrientation == 270, "$imgDir/${imgName}.jpg", { savedPath, time ->
                    Toasts.show("图片保存成功！ 保存路径：$savedPath 耗时：$time")
                    onPictureCapturedAction?.invoke(savedPath)
                }, { msg ->
                    Toasts.show("图片保存失败！ $msg -- $imgName")
                })
                isTakingPic = false
            }
            close()
        }

    }

    /**
     * 打开相机
     */
    @SuppressLint("MissingPermission")
    private fun openCamera() {
        mediaRecorder = MediaRecorder()

        mCameraManager.openCamera(mCameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                loge("onOpened")
                mCameraDevice = camera
                startPreviewSession(camera)
            }

            override fun onDisconnected(camera: CameraDevice) {
                loge("onDisconnected")
            }

            override fun onError(camera: CameraDevice, error: Int) {
                loge("onError $error")
                Toasts.show("打开相机失败！$error")
            }
        }, mCameraHandler)
    }

    /**
     * 创建预览会话
     */
    private fun startPreviewSession(cameraDevice: CameraDevice) {

        tryCatch({
            closePreviewSession()
            val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

            val surface = Surface(mTextureView.surfaceTexture)
            captureRequestBuilder.addTarget(surface)  // 将CaptureRequest的构建器与Surface对象绑定在一起
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)      // 闪光灯
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦

            // 为相机预览，创建一个CameraCaptureSession对象
            cameraDevice.createCaptureSession(listOf(surface, mImageReader?.surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Toasts.show("开启预览会话失败！")
                }

                override fun onConfigured(session: CameraCaptureSession) {
                    loge("onConfigured")
                    mCameraCaptureSession = session
                    mPreviewRequest = captureRequestBuilder.build()
                    session.setRepeatingRequest(mPreviewRequest!!, mCaptureCallBack, mCameraHandler)
                }

            }, mCameraHandler)
        }, {
            loge("startPreviewSession $it")
        })
    }

    private fun closePreviewSession() {
        mCameraCaptureSession?.close()
        mCameraCaptureSession = null
    }


    private val mCaptureCallBack = object : CameraCaptureSession.CaptureCallback() {

        override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
            super.onCaptureCompleted(session, request, result)
            canExchangeCamera = true
            canTakePic = true
        }

        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
            loge("onCaptureFailed")
            Toasts.show("开启预览失败！")
        }
    }


    /**
     *
     * 根据提供的参数值返回与指定宽高相等或最接近的尺寸
     *
     * @param targetWidth   目标宽度
     * @param targetHeight  目标高度
     * @param maxWidth      最大宽度(即TextureView的宽度)
     * @param maxHeight     最大高度(即TextureView的高度)
     * @param sizeList      支持的Size列表
     *
     * @return  返回与指定宽高相等或最接近的尺寸
     *
     */
    private fun getBestSize(targetWidth: Int, targetHeight: Int, maxWidth: Int, maxHeight: Int, sizeList: List<Size>): Size {
        val bigEnough = ArrayList<Size>()     //比指定宽高大的Size列表
        val notBigEnough = ArrayList<Size>()  //比指定宽高小的Size列表

        for (size in sizeList) {

            //宽<=最大宽度  &&  高<=最大高度  &&  宽高比 == 目标值宽高比
            if (size.width <= maxWidth && size.height <= maxHeight
                && size.width * targetHeight == targetWidth * size.height ) {


                if (size.width >= targetWidth && size.height >= targetHeight)
                    bigEnough.add(size)
                else
                    notBigEnough.add(size)
            }
            loge("系统支持的尺寸: ${size.width} * ${size.height} ,  比例 ：${size.width.toFloat() / size.height}")
        }

        loge("最大尺寸 ：$maxWidth * $maxHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")
        loge("目标尺寸 ：$targetWidth * $targetHeight, 比例 ：${targetWidth.toFloat() / targetHeight}")
        //选择bigEnough中最小的值  或 notBigEnough中最大的值
        return when {
            bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
            notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
            else -> sizeList[0]
        }
    }

    /**
     * 根据提供的屏幕方向 [displayRotation] 和相机方向 [sensorOrientation] 返回是否需要交换宽高
     */
    private fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {
        var exchange = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true
                }
            Surface.ROTATION_90, Surface.ROTATION_270 ->
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true
                }
            else -> loge("Display rotation is invalid: $displayRotation")
        }

        loge("屏幕方向  $displayRotation")
        loge("相机方向  $sensorOrientation")
        loge("exchange  $exchange")
        return exchange
    }


    private fun releaseCamera() {

        closePreviewSession()

        mediaRecorder?.release()
        mediaRecorder = null

        mCameraDevice?.close()
        mCameraDevice = null

        mImageReader?.close()
        mImageReader = null

        canExchangeCamera = false
    }

    fun releaseThread() {
        handlerThread.quitSafely()
    }

    fun startRecordingVideo() {
        if (mCameraDevice == null || !mTextureView.isAvailable) return

        try {
            closePreviewSession()
            setUpMediaRecorder()
            val texture = mTextureView.surfaceTexture.apply {
                setDefaultBufferSize(mPreviewSize.width, mPreviewSize.height)
            }
            val previewSurface = Surface(texture)
            val recorderSurface = mediaRecorder!!.surface
            val surfaces = ArrayList<Surface>().apply {
                add(previewSurface)
                add(recorderSurface)
            }

            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW).apply {
                addTarget(previewSurface)
                addTarget(recorderSurface)
            }
            loge("startRecordingVideo ${recorderSurface.isValid}")

            mCameraDevice?.createCaptureSession(surfaces,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigureFailed(session: CameraCaptureSession) {

                    }

                    override fun onConfigured(session: CameraCaptureSession) {
                        mCameraCaptureSession = session
                        updatePreview()
                        mediaRecorder?.start()
                        videoRecordStartTime = System.currentTimeMillis()
                    }

                }, mCameraHandler)

        }catch (e: CameraAccessException) {
            loge(e)
        }catch (e: IOException) {
            loge(e)
        }
    }

    fun stopRecordingVideo() {
        mCameraCaptureSession?.abortCaptures()
        mCameraCaptureSession?.stopRepeating()

        mediaRecorder?.apply {
            stop()
            reset()
        }
        Toasts.show("Video saved: $nextVideoAbsolutePath")
        onVideoCapturedAction?.invoke(nextVideoAbsolutePath!!,
            ((System.currentTimeMillis() - videoRecordStartTime) / 1000).toInt()
        )
        nextVideoAbsolutePath = null

    }

    private fun updatePreview() {
        if (mCameraDevice == null) return

        try {
            captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
            HandlerThread("CameraPreview").start()
            mPreviewRequest = captureRequestBuilder.build()
            mCameraCaptureSession?.setRepeatingRequest(mPreviewRequest!!,
                null, mCameraHandler)
        } catch (e: CameraAccessException) {
            loge("updatePreview ${e.message}")
        }

    }

    private var nextVideoAbsolutePath: String ? = null

    private fun setUpMediaRecorder() {
        if (nextVideoAbsolutePath.isNullOrEmpty()) {
            nextVideoAbsolutePath = getVideoFilePath(context)
        }
        val rotation = context.windowManager.defaultDisplay.rotation
        when (mCameraSensorOrientation) {
            SENSOR_ORIENTATION_DEFAULT_DEGREES ->
                mediaRecorder?.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation))
            SENSOR_ORIENTATION_INVERSE_DEGREES ->
                mediaRecorder?.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation))
        }

        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(nextVideoAbsolutePath)
            setVideoEncodingBitRate(10000000)
            setVideoFrameRate(30)
            setVideoSize(mSavePicSize.width, mSavePicSize.height)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
        }
    }

    private fun getVideoFilePath(context: Context?): String {
        val filename = "${System.currentTimeMillis()}.mp4"
        val dir = context?.getExternalFilesDir(VIDEO_PATH)

        return if (dir == null) {
            filename
        } else {
            "${dir.absolutePath}/$filename"
        }
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }
}