package com.benyq.module_base.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.module_base.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.tabs.TabLayout

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

inline fun View.setThrottleClickListener(
    throttle: Long = 500,
    crossinline onClick: (v: View) -> Unit
) {
    setOnClickListener(object : View.OnClickListener {
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

fun TextView.setTextAppearanceCustomer(context: Context, @StyleRes resId: Int) {
    if (fromM()) {
        this.setTextAppearance(resId)
    } else {
        this.setTextAppearance(context, resId)
    }
}


/**
 * 设置TextView图标
 *
 * @param drawable     图标
 * @param iconWidth    图标宽dp：默认自动根据图标大小
 * @param iconHeight   图标高dp：默认自动根据图标大小
 * @param direction    图标方向，0左 1上 2右 3下 默认图标位于左侧0
 */
fun TextView.setDrawable(
    drawable: Drawable?,
    iconWidth: Float? = null,
    iconHeight: Float? = null,
    direction: Int = 0
) {
    if (iconWidth != null && iconHeight != null) {
        //第一个0是距左边距离，第二个0是距上边距离，iconWidth、iconHeight 分别是长宽
        drawable?.setBounds(
            0,
            0,
            context.dip2px(iconWidth).toInt(),
            context.dip2px(iconHeight).toInt()
        )
    }
    when (direction) {
        0 -> setCompoundDrawables(drawable, null, null, null)
        1 -> setCompoundDrawables(null, drawable, null, null)
        2 -> setCompoundDrawables(null, null, drawable, null)
        3 -> setCompoundDrawables(null, null, null, drawable)
        else -> throw NoSuchMethodError()
    }
}

fun TabLayout.setTextStyleSelectState(position: Int, @StyleRes style: Int) {
    val title =
        ((getChildAt(0) as LinearLayout).getChildAt(position) as LinearLayout).getChildAt(
            1
        ) as TextView
    title.setTextAppearanceCustomer(context, style)
}

fun ViewPager2.overScrollNever() {
    val child: View = getChildAt(0)
    (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
}

/**
 * 占位隐藏view，带有渐隐动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.invisibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.INVISIBLE
    this?.startAnimation(AlphaAnimation(1f, 0f).apply {
        this.duration = duration
        fillAfter = true
    })
}

/**
 * 显示view，带有渐显动画效果。
 *
 * @param duration 毫秒，动画持续时长，默认500毫秒。
 */
fun View?.visibleAlphaAnimation(duration: Long = 500L) {
    this?.visibility = View.VISIBLE
    this?.startAnimation(AlphaAnimation(0f, 1f).apply {
        this.duration = duration
        fillAfter = true
    })
}

fun ImageView.loadImage(
    url: String?,
    @DrawableRes
    resId: Int = 0,
    round: Int = 10,
    isCircle: Boolean = false,
    placeHolder: Int = R.drawable.shape_album_loading_bg,
    error: Int = R.drawable.shape_album_loading_bg
) {
    Glide.with(context).load(if (url.isNullOrEmpty()) resId else url)
        .placeholder(placeHolder)
        .error(error)
        .run {
            when {
                isCircle -> {
                    transform(CircleCrop())
                }
                round > 0 -> {
                    transform(RoundedCorners(context.dip2px(round).toInt()))
                }
                else -> {
                    this
                }
            }
        }
        .into(this)
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