package com.benyq.guochat.function.media

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.Surface
import android.view.TextureView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.benyq.guochat.app.IMG_PATH
import com.benyq.guochat.function.other.DateFormatUtil
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
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

    private lateinit var mCameraManager: CameraManager
    private var mImageReader: ImageReader? = null
    private var mCameraDevice: CameraDevice? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mPreviewRequest: CaptureRequest? = null

    private var mCameraId = "0"
    private lateinit var mCameraCharacteristics: CameraCharacteristics

    private var mCameraSensorOrientation = 0                                            //摄像头方向
    private var mCameraFacing = CameraCharacteristics.LENS_FACING_BACK              //默认使用后置摄像头
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
    private var onVideoCapturedAction : ((String)->Unit) ?= null

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

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        loge("VideoCaptureManager onPause")
//        mCameraCaptureSession?.stopRepeating()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        releaseCamera()
        releaseThread()
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
        try {
            //创建捕获请求
            //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
            mCameraCaptureSession?.setRepeatingRequest(mPreviewRequest!!, null, null)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }


    fun setPictureCapturedAction(action: (String) -> Unit) {
        onPictureCapturedAction = action
    }

    fun setVideoCapturedAction(action: (String) -> Unit) {
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
        val previewSize = configurationMap!!.getOutputSizes(SurfaceTexture::class.java) //预览尺寸

        val exchange = exchangeWidthAndHeight(mDisplayRotation, mCameraSensorOrientation)

        mSavePicSize = getBestSize(
            if (exchange) mSavePicSize.height else mSavePicSize.width,
            if (exchange) mSavePicSize.width else mSavePicSize.height,
            if (exchange) mSavePicSize.height else mSavePicSize.width,
            if (exchange) mSavePicSize.width else mSavePicSize.height,
            savePicSize.toList())

        mPreviewSize = getBestSize(
            if (exchange) mPreviewSize.height else mPreviewSize.width,
            if (exchange) mPreviewSize.width else mPreviewSize.height,
            if (exchange) mTextureView.height else mTextureView.width,
            if (exchange) mTextureView.width else mTextureView.height,
            previewSize.toList())

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

        mCameraManager.openCamera(mCameraId, object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                loge("onOpened")
                mCameraDevice = camera
                createCaptureSession(camera)
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
    private fun createCaptureSession(cameraDevice: CameraDevice) {

        val captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

        val surface = Surface(mTextureView.surfaceTexture)
        captureRequestBuilder.addTarget(surface)  // 将CaptureRequest的构建器与Surface对象绑定在一起
        captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)      // 闪光灯
        captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦

        // 为相机预览，创建一个CameraCaptureSession对象
        cameraDevice.createCaptureSession(arrayListOf(surface, mImageReader?.surface), object : CameraCaptureSession.StateCallback() {
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
                && size.width == size.height * targetWidth / targetHeight) {

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
        return exchange
    }


    fun releaseCamera() {
        mCameraCaptureSession?.close()
        mCameraCaptureSession = null

        mCameraDevice?.close()
        mCameraDevice = null

        mImageReader?.close()
        mImageReader = null

        canExchangeCamera = false
    }

    fun releaseThread() {
        handlerThread.quitSafely()
    }

    private class CompareSizesByArea : Comparator<Size> {
        override fun compare(size1: Size, size2: Size): Int {
            return java.lang.Long.signum(size1.width.toLong() * size1.height - size2.width.toLong() * size2.height)
        }
    }
}