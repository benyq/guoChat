package com.benyq.guochat.chat.ui.discover

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.annotation.ColorInt
import kotlin.math.abs


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
    private var touchSlop = ViewConfiguration.get(context).scaledTouchSlop

    private var mOnChangePaintStatusAction: ((Boolean)->Unit)? = null
    private var mOnHideOtherViewAction: (()->Unit)? = null

    private var actionDownX = 0f
    private var actionDownY = 0f

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {

            val touchX = x
            val touchY = y

            when(action) {
                MotionEvent.ACTION_DOWN -> {
                    actionDownX = touchX
                    actionDownY = touchY

                    paintPathData = generatePaintPathData().apply {
                        path.moveTo(touchX, touchY)
                        paintPathList.add(this)
                    }

                    Log.e("PainterView", "onTouchEvent: down")
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    mOnHideOtherViewAction?.invoke()
                    paintPathData?.path?.lineTo(touchX, touchY)
                    Log.e("PainterView", "onTouchEvent: move")
                }
                MotionEvent.ACTION_UP -> {
                    if (abs(touchX - actionDownX) > touchSlop || abs(touchY - actionDownY) > touchSlop) {
                        //手指滑动了
                        Log.e("PainterView", "onTouchEvent: 手指滑动了")
                    }else {
                        Log.e("PainterView", "onTouchEvent: 手指没滑动")
                        changePaintStatus()
                    }
                    Log.e("PainterView", "onTouchEvent: up")
                }
                MotionEvent.ACTION_CANCEL -> {
                    Log.e("PainterView", "onTouchEvent: cancel")
                }
                else -> {
                    Log.e("PainterView", "onTouchEvent: else ${action}")
                }
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

    fun setOnChangePaintStatusAction(action: (Boolean)->Unit) {
        mOnChangePaintStatusAction = action
    }

    fun setOnHideOtherViewAction(action: ()->Unit) {
        mOnHideOtherViewAction = action
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
    private fun changePaintStatus() {
        mOnChangePaintStatusAction?.invoke(false)
    }
}