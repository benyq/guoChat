package com.benyq.mvvm.ext

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */


/**
 *  防止重复 showToast 显示
 *  如果 mToast不为空 就显示, 否则就创建新的 mToast
 */
fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toasts.showToast(this, message, duration)
}

fun Context.toast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(message), duration)
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    context?.toast(message, duration)
}

fun Fragment.toast(@StringRes strRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(strRes), duration)
}
