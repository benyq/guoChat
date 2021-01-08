package com.benyq.mvvm.ext

import android.app.Dialog
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import androidx.annotation.FloatRange
import kotlin.math.log

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/1/11
 * description: 系统拓展方法
 */


fun Context.versionCode(): String {
    val packageManager = packageManager
    val packageInfo: PackageInfo
    var versionCode = ""
    try {
        packageInfo = packageManager.getPackageInfo(packageName, 0)
        versionCode = packageInfo.versionCode.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

fun Context.versionName(): String {
    val packageManager = packageManager
    val packageInfo: PackageInfo
    var versionName = ""
    try {
        packageInfo = packageManager.getPackageInfo(packageName, 0)
        versionName = packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionName
}

private val handler = Handler(Looper.getMainLooper())
fun runOnUiThread(duration: Long = 0L, block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        handler.postDelayed(block, duration)
    }
}

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo? = cm.activeNetworkInfo
    return info?.isConnected ?: false
}

fun Context.changeSize(
    dialog: Dialog,
    @FloatRange(from = 0.0, to = 1.0) width: Float,
    isSquare: Boolean = false
) {
    val window = dialog.window
    window?.let {
        val lp = it.attributes
        //横屏
        lp.width = (getScreenWidth() * width).toInt()
        if (isSquare) {
            lp.height = lp.width
        }
        lp.gravity = Gravity.CENTER
        it.attributes = lp
    }
}

fun Context.changeSize(
    dialog: Dialog,
    @FloatRange(from = 0.0, to = 1.0) width: Float,
    @FloatRange(from = 0.0, to = 1.0) height: Float
) {
    val window = dialog.window
    window?.let {
        val lp = it.attributes
        //横屏
        lp.width = (getScreenWidth() * width).toInt()
        lp.height = (getScreenHeight() * height).toInt()
        lp.gravity = Gravity.CENTER
        it.attributes = lp
    }
}


fun Context.getScreenWidth(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getRealSize(point)
    return point.x
}

fun Context.getScreenHeight(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getRealSize(point)
    return point.y
}

fun Context.getScreenSize(): Point {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val point = Point()
    wm.defaultDisplay.getRealSize(point)
    return point
}

//判断是否是全面屏
fun Context.checkFullScreen(): Boolean {
    val point = getScreenSize()
    val width: Int
    val height: Int
    if (point.x < point.y) {
        width = point.x
        height = point.y
    }else {
        width = point.y
        height = point.x
    }
    loge("width $width height $height")
    return height.toFloat() / width > 1.97f
}

fun Context.isNetWorkConnected(): Boolean {
    val cm: ConnectivityManager? =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return cm?.activeNetworkInfo?.isConnected ?: false
}

fun Context.getVersionCode(): String {
    val packageManager = packageManager
    val packageInfo: PackageInfo
    var versionCode = ""
    try {
        packageInfo = packageManager.getPackageInfo(packageName, 0)
        versionCode = packageInfo.versionCode.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return versionCode
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 *
 * @param dipValue
 * （DisplayMetrics类中属性density）
 * @return
 */
fun Context.dip2px(dipValue: Float): Float {
    val scale = resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}

fun Context.dip2px(dipValue: Int): Float {
    val scale = resources.displayMetrics.density
    return (dipValue * scale + 0.5f)
}

fun fromM() = fromSpecificVersion(Build.VERSION_CODES.M)
fun fromQ() = fromSpecificVersion(Build.VERSION_CODES.Q)
fun beforeM() = beforeSpecificVersion(Build.VERSION_CODES.M)
fun beforeQ() = beforeSpecificVersion(Build.VERSION_CODES.Q)
fun fromN() = fromSpecificVersion(Build.VERSION_CODES.N)
fun beforeN() = beforeSpecificVersion(Build.VERSION_CODES.N)
fun fromO() = fromSpecificVersion(Build.VERSION_CODES.O)
fun beforeO() = beforeSpecificVersion(Build.VERSION_CODES.O)
fun fromP() = fromSpecificVersion(Build.VERSION_CODES.P)
fun beforeP() = beforeSpecificVersion(Build.VERSION_CODES.P)
fun fromSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT >= version
fun beforeSpecificVersion(version: Int): Boolean = Build.VERSION.SDK_INT < version