package com.benyq.guochat.chat.ui.discover

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt

/**
 * @author benyq
 * @time 2021/7/18
 * @e-mail 1520063035@qq.com
 * @note
 */

data class PaintPathData(val paint: Paint, val path: Path)

class PaintPathView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr){

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val paintPathList = mutableListOf<PaintPathData>()

    private var paintPathData : PaintPathData? = null
    private var mBitmap: Bitmap? = null
    private var mBitmapMatrix: Matrix? = null

    @ColorInt
    private var mPaintColor: Int = Color.BLUE

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {

            val touchX = x
            val touchY = y

            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    paintPathData = generatePaintPathData().apply {
                        path.moveTo(touchX, touchY)
                        paintPathList.add(this)
                    }

                    Log.e("PainterView", "onTouchEvent: down")
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    dismissController()
                    paintPathData?.path?.lineTo(touchX, touchY)
                    Log.e("PainterView", "onTouchEvent: move")
                }
                MotionEvent.ACTION_UP -> {
                    dismissController()
                    Log.e("PainterView", "onTouchEvent: up")
                }
                MotionEvent.ACTION_CANCEL -> {

                }
                else -> {}
            }
        }
        invalidate()
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mBitmapMatrix != null && mBitmap != null) {
            canvas?.drawBitmap(mBitmap!!, mBitmapMatrix!!, null)
        }
        paintPathList.forEach {
            canvas?.drawPath(it.path, it.paint)
        }
    }

    fun resetPath() {
        paintPathData = null
        paintPathList.clear()
        invalidate()
    }


    fun setBitmap(bitmap: Bitmap) {
        mBitmap = bitmap
        invalidate()
    }

    fun setBitmapMatrix(matrix: Matrix) {
        mBitmapMatrix = matrix
    }


    fun setPaintColor(@ColorInt color: Int) {
        mPaintColor = color
    }

    fun hasEdited() = paintPathList.isNotEmpty()

    private fun generatePaintPathData(): PaintPathData {
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 10f
            color = mPaintColor
        }
        val path = Path()
        return PaintPathData(paint, path)
    }

    /**
     * 通知外部控制器隐藏
     */
    private fun dismissController() {

    }
}