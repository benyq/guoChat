package com.benyq.module_base.ui.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.benyq.module_base.ext.loge
import kotlin.math.abs
import kotlin.math.max

/**
 * @author benyq
 * @time 2020/10/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class PuzzleImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val mCaptchaPath: Path = Path()

    /**
     * 最小距离
     */
    private var minDistance = -1

    /**
     * 保存缺块信息
     */
    private val mPositionInfo = PositionInfo()

    private var mCoverBitmap: Bitmap? = null
    private var mCoverLeft = 0f

    private val mPaint: Paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        strokeWidth = 5f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mPositionInfo.createInfo(w, h)
        createCaptchaPath()
        mCoverLeft = -mPositionInfo.left
        if (drawable != null) {
            getMaskBitmap(mCaptchaPath)
        }
        minDistance = max((mPositionInfo.width / 15).toInt(), 6)
    }

    private fun createCaptchaPath() {
        mCaptchaPath.run {
            reset()
            moveTo(mPositionInfo.left, mPositionInfo.top)
            lineTo(mPositionInfo.left + mPositionInfo.width, mPositionInfo.top)
            lineTo(
                mPositionInfo.left + mPositionInfo.width,
                mPositionInfo.top + mPositionInfo.height
            )
            lineTo(mPositionInfo.left, mPositionInfo.top + mPositionInfo.height)

        }

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mCaptchaPath, mPaint)
        mCoverBitmap?.run {
            canvas?.drawBitmap(this, mCoverLeft, 0f, mPaint)
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        loge("prepare onWindowFocusChanged")
        prepare()
    }

    fun setMaskRadio(radio: Float) {
        mCoverLeft = radio * width - mPositionInfo.left
        invalidate()
    }

    fun checkResult() : Boolean = abs(mCoverLeft) <= minDistance

    fun prepare() : Boolean{
        if ( mCoverBitmap != null || width <= 0 || height <= 0 || drawable == null) {
            return false
        }
        loge("prepare  getMaskBitmap ")
        mCoverBitmap = getMaskBitmap(mCaptchaPath)
        return true
    }

    //这种模式可以生成不规则的图案
    private fun getMaskBitmap(mCaptchaPath: Path): Bitmap {
        //按照控件的宽高 创建一个 bitmap
        val tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        //将 创建 bitmap 作为画板
        val c = Canvas(tempBitmap)
        //抗锯齿
        c.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        //绘制用于遮罩的圆形
        c.drawPath(mCaptchaPath, mPaint)
        //设置遮罩模式
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        //考虑 scaleType 等因素 ，要用 Matrix 对 Bitmap 进行缩放
        c.drawBitmap(drawable.toBitmap(width, height), imageMatrix, mPaint)
        mPaint.xfermode = null
        return tempBitmap
    }



    inner class PositionInfo {
        //缺块在整张图片的左上角x轴位置
        var left = 0f
        //缺块在整张图片的左上角y轴位置
        var top = 0f

        var width = 0f
        var height = 0f

        fun createInfo(viewWidth: Int, viewHeight: Int) {

            val xRadio = (Math.random() * 40 + 40).toFloat() / 100
            val yRadio = 0.5f

            left = viewWidth * xRadio
            top = viewHeight * yRadio

            width = viewWidth * 0.1f
            height = viewHeight * 0.1f

            if (width > height) {
                height = width
            }else {
                width = height
            }
        }
    }
}