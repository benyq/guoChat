package com.benyq.mvvm.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

/**
 * @author benyq
 * @time 2020/3/2
 * @e-mail 1520063035@qq.com
 * @note
 */

// R.color.xxx  -> @ColorInt
fun Context.getColorRef(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this, res)
}

// R.drawable.xxx -> Drawable
fun Context.getDrawableRef(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this, res)
}

fun View.getColorRef(@ColorRes res: Int): Int {
    return ContextCompat.getColor(this.context, res)
}

fun View.getDrawableRef(@DrawableRes res: Int): Drawable? {
    return ContextCompat.getDrawable(this.context, res)
}