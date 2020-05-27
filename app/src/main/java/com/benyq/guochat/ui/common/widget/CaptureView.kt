package com.benyq.guochat.ui.common.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.benyq.guochat.dip2px

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class CaptureView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : View(context, attrs, defStyleAttr){

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    /**
     * 最大摄像时间 / 秒
     */
    private val maxCaptureTime = 30F

    /**
     * 正常时内部元半径
     */
    private var mNormalInnerCircleRadius = dip2px(context, 30)

    /**
     * 正常时外部圆半径
     */
    private var mNormalOuterCircleRadius = dip2px(context, 41)

    /**
     * 拍摄时内部元半径
     */
    private var mCaptureInnerCircleRadius = dip2px(context, 15)

    /**
     * 拍摄时外部圆半径
     */
    private var mCaptureOuterCircleRadius = dip2px(context, 55)

    private var mProgressRadius = dip2px(context, 50)

    private val mInnerCircleColor = Color.WHITE
    private val mOuterCircleColor = Color.parseColor("#EDEDED")

    private var mViewWidth = 0F
    private var mViewHeight = 0F

    /**
     * 圆形画笔
     */
    private var mCirclePaint = Paint().apply {
        style = Paint.Style.FILL
    }

    /**
     * 进度条画笔
     */
    private var mProgressPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.parseColor("#6200EE")
        strokeWidth = dip2px(context, 5)
    }

    private lateinit var mProgressRectF: RectF

    private var isCapture = false

    /**
     * 当前摄像时间
     */
    private var mCaptureTime = 0F

    private val mHandler = Handler()
    private val mTimingRunnable = Runnable {
        startVideo()
    }

    /**
     * 最大摄像时间结束
     */
    private val mMaxViewRunnable = Runnable {
        stopVideo()
    }

    private val mCapturingRunnable = object: Runnable {
        override fun run() {
            mCaptureTime += 0.1F
            invalidate()
            mHandler.postDelayed(this, 100L)
        }
    }

    /**
     * 开始拍照
     */
    private var mPictureAction : (()->Unit) ? = null
    /**
     * 开始摄像
     */
    private var mVideoStartAction : (()->Unit) ? = null
    /**
     * 开始拍照
     */
    private var mVideoEndAction : (()->Unit) ? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w.toFloat()
        mViewHeight = h.toFloat()
        mProgressRectF = RectF(-mProgressRadius, -mProgressRadius, mProgressRadius, mProgressRadius)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            //坐标移到中心
            it.translate(mViewWidth / 2, mViewHeight / 2)
            if (isCapture) {
                mCirclePaint.color = mOuterCircleColor
                it.drawCircle(0f, 0f, mCaptureOuterCircleRadius, mCirclePaint)
                mCirclePaint.color = mInnerCircleColor
                it.drawCircle(0f, 0f, mCaptureInnerCircleRadius, mCirclePaint)
                it.drawArc(mProgressRectF, -90F, (mCaptureTime / maxCaptureTime) * 360, false, mProgressPaint)

            }else {
                //正常情况
                mCirclePaint.color = mOuterCircleColor
                it.drawCircle(0f, 0f, mNormalOuterCircleRadius, mCirclePaint)
                mCirclePaint.color = mInnerCircleColor
                it.drawCircle(0f, 0f, mNormalInnerCircleRadius, mCirclePaint)
            }
        }
    }

    private var mOnTouchTime = 0L

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //500ms以内都算拍照，500ms之后算摄像
        val onClickTime = 500L
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isCapture = true
                mOnTouchTime = System.currentTimeMillis()
                //开始Handler计时，如果1s后没有取消，则开始拍照
                mHandler.postDelayed(mTimingRunnable, onClickTime)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mHandler.removeCallbacks(mTimingRunnable)
                val interval = System.currentTimeMillis() - mOnTouchTime
                if (interval <= onClickTime) {
                    //拍照
                    mPictureAction?.invoke()
                }else {
                    //摄像
                    stopVideo()
                }
                isCapture = false
                invalidate()
            }
        }
        return true
    }


    fun setPictureAction(action: ()->Unit) {
        mPictureAction = action
    }

    fun setVideoStartAction(action: ()->Unit) {
        mVideoStartAction = action
    }

    fun setVideoEndAction(action: ()->Unit) {
        mVideoEndAction = action
    }

    private fun stopVideo() {
        mHandler.removeCallbacks(mMaxViewRunnable)
        mHandler.removeCallbacks(mCapturingRunnable)
        mCaptureTime = 0F
        mVideoEndAction?.invoke()
        isCapture = false
        invalidate()
    }

    private fun startVideo() {
        mVideoStartAction?.invoke()
        mHandler.postDelayed(mMaxViewRunnable, (maxCaptureTime * 1000).toLong())
        mHandler.postDelayed(mCapturingRunnable, 100L)
        invalidate()
    }
}