package com.benyq.mvvm.ext

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note Toast
 */
object Toasts {

    private var mToast: Toast? = null

    @SuppressLint("ShowToast")
    fun init(context: Context) {
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
    }

    fun show(message: String) {
        runOnUiThread {
            mToast?.apply { setText(message) }?.show()
        }
    }

    fun show(@StringRes stringId: Int) {
        runOnUiThread {
            mToast?.apply { setText(stringId) }?.show()
        }
    }


    /**
     *  如果 mToast 没有初始化, 就创建一个 Toast, 并赋值
     *  否则就直接显示
     */
    fun <T : Context> showToast(context: T, message: String, duration: Int) {
        mToast?.let {
            it.duration = duration
            it.setText(message)
            it.show()
        } ?: Toast.makeText(context, message, duration).apply {
            mToast = this
            show()
        }
    }


}