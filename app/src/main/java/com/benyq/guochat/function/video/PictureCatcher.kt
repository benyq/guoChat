package com.benyq.guochat.function.video

import android.graphics.Bitmap
import android.opengl.GLES20
import android.util.Log
import com.benyq.guochat.function.video.drawer.VideoDrawer
import com.benyq.mvvm.ext.logw
import com.benyq.mvvm.ext.tryCatch
import okio.buffer
import okio.sink
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.microedition.khronos.opengles.GL10

/**
 * @author benyq
 * @time 2021/2/6
 * @e-mail 1520063035@qq.com
 * @note 图片采集逻辑
 */

/**
 * 参数 1  图片
 * 参数 2  图片保存本地地址
 */
typealias TakePictureSuccessListener = (Bitmap, String)->Unit
typealias TakePictureErrorListener = (Throwable)->Unit


object PictureCatcher {

    private const val TAG = "PictureCatcher"

    private var mTakePictureSuccessListener: TakePictureSuccessListener? = null
    private var mTakePictureErrorListener: TakePictureErrorListener? = null
    private var takePicFlag = false
    private var mPicturePath: String = ""


    /**
     * @param path 图片路径
     * @param success 图片保存监听
     * @param error 错误
     */
    fun setCatchParams(path: String, success: TakePictureSuccessListener, error: TakePictureErrorListener) {
        //目前只支持jpeg
        if (!path.contains(".jpeg")) {
            logw("takePicture 目前只支持jpeg")
            return
        }
        takePicFlag = true
        mTakePictureSuccessListener = success
        mTakePictureErrorListener = error
    }

    fun takePicture(textureId: Int,
                            width: Int,
                            height: Int,
                            mvpMatrix: FloatArray?,
                            texMatrix: FloatArray?, isFrontCamera: Boolean) {
        if (!takePicFlag || mPicturePath.isEmpty()){
            return
        }

        takePicFlag = false

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
        if (isFrontCamera) {
            matrix.postRotate(270f)
        }else {
            matrix.postRotate(90f)
        }
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