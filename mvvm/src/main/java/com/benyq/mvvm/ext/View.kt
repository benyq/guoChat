package com.benyq.mvvm.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.StyleRes

/**
 *
 * @author benyq
 * @time 2020/3/1
 * @e-mail 1520063035@qq.com
 * @note 有关View的拓展方法
 */

/**
 *
 * @author benyq
 * @time 2020/2/29
 * @e-mail 1520063035@qq.com
 * @note
 */

fun EditText.textAndSelection(value: String?) {
    value?.let {
        setText(value)
        setSelection(value.length)
    }

}

fun TextView.textTrim() = text.toString().trim()

// 关闭软键盘
fun View.hideKeyBoard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

// 显示软键盘
fun View.showKeyboard() {
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

// 设置双击事件
fun View.setDoubleClickListener(block: () -> Unit) {
    this.setOnClickListener { ClickUtil.interval(block) }
}

inline fun View.setThrottleClickListener(throttle: Long = 500, crossinline onClick: (v: View) ->Unit) {
    setOnClickListener(object: View.OnClickListener{
        private var prevClickTime = 0L
        override fun onClick(v: View?) {
            val t = System.currentTimeMillis()
            if (t - throttle > prevClickTime) {
                prevClickTime = t
                onClick(this@setThrottleClickListener)
            }
        }
    })
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun TextView.setTextAppearanceCustomer(context: Context, @StyleRes resId:Int ){
    if (fromM()){
        this.setTextAppearance(resId)
    }else {
        this.setTextAppearance(context, resId)
    }
}


/**
 *
 * @author benyq
 * @time 2020/3/1
 * @e-mail 1520063035@qq.com
 * @note
 */
object ClickUtil {

    private var clickTime: Long = 0

    /**
     *  双击事件
     *  @param duration 两次间隔时间
     */
    fun interval(block: () -> Unit, duration: Int = 1000) {
        val nowTime = System.currentTimeMillis()
        if (nowTime - clickTime > duration) {
            clickTime = nowTime
        } else {
            block()
        }
    }

}