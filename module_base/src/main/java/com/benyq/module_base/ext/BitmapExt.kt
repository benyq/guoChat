package com.benyq.module_base.ext

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

/**
 * @author benyq
 * @time 2020/12/17
 * @e-mail 1520063035@qq.com
 * @note
 */

//水平镜像翻转
fun Bitmap.mirror(): Bitmap {
    val matrix = Matrix()
    matrix.postScale(-1f, 1f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

//旋转,水平镜像翻转
fun Bitmap.rotate(degree: Float, mirror: Boolean = false): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degree)
    if (mirror) {
        matrix.postScale(-1f, 1f)
    }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.toByteArray(): ByteArray {
    var os = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, os)
    return os.toByteArray()
}
