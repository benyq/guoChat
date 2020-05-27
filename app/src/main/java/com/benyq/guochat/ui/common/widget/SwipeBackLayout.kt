package com.benyq.guochat.ui.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @author benyq
 * @time 2020/5/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class SwipeBackLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ViewGroup(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childSize = childCount
        for (position in 0 until childSize) {
            val view = getChildAt(position)
            view.layout(l, t, r, b)
        }
    }
}