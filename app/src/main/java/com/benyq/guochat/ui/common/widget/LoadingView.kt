package com.benyq.guochat.ui.common.widget

import android.animation.ArgbEvaluator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.benyq.guochat.dip2px
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/7/15
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoadingView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val argbEvaluator = ArgbEvaluator()
    private val startColor = Color.parseColor("#DDDDDD")
    private val endColor = Color.parseColor("#333333")
    private var centerX = 0
    private var centerY = 0

    var time = 0 // 重复次数

    private val lineCount = 12
    private val avgAngle: Float = 360f / lineCount
    private var radius = 0
    private var radiusOffset = 0f
    private var stokeWidth = 0f

    var startX = 0f
    var endX = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val increaseTask = Runnable {
        time++
        postInvalidate()
    }

    init {
        paint.strokeWidth = 0f
        paint.strokeCap = Paint.Cap.ROUND

        removeCallbacks(increaseTask)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        radius = w / 2
        radiusOffset = radius / 2f

        stokeWidth = w * 1f / 15
        paint.strokeWidth = stokeWidth

        centerX = w / 2
        centerY = h / 2

        startX = centerX + radiusOffset
        endX = startX + radius / 3f

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //先画
        for (i in lineCount - 1 downTo 0) {
            val temp: Int = abs(i + time) % lineCount
            val fraction = (temp + 1) * 1f / lineCount
            val color = argbEvaluator.evaluate(fraction, startColor, endColor) as Int
            paint.color = color
            canvas!!.drawLine(startX, centerY.toFloat(), endX, centerY.toFloat(), paint)
            canvas.rotate(avgAngle, centerX.toFloat(), centerY.toFloat())
        }

        postDelayed(increaseTask, 50)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        removeCallbacks(increaseTask)
    }
}