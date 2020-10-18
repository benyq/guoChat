package com.benyq.guochat.comic.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/10/11
 * @e-mail 1520063035@qq.com
 * @note  可以点击的RecyclerView
 */
class TouchRecyclerView(context: Context, @Nullable attrs: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attrs, defStyle) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var mTouchX = 0f
    private var mTouchY = 0f

    private var mTouchCallBack: (()->Unit)? = null

    fun setTouchCallback(callback: ()->Unit) {
        mTouchCallBack = callback
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        e?.run {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    mTouchX = x
                    mTouchY = y
                }
                MotionEvent.ACTION_UP -> {
                    if (abs(x - mTouchX) <= touchSlop && abs(y - mTouchY) <= touchSlop) {
                        mTouchCallBack?.invoke()
                    }
                }
            }
        }
        return super.onTouchEvent(e)
    }
}