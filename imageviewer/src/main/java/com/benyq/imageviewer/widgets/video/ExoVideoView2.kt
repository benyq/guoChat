package com.benyq.imageviewer.widgets.video

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class ExoVideoView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ExoVideoView(context, attrs, defStyleAttr), View.OnTouchListener {

    interface Listener {
        fun onDrag(view: ExoVideoView2, fraction: Float)
        fun onRestore(view: ExoVideoView2, fraction: Float)
        fun onRelease(view: ExoVideoView2, fraction: Float)
    }

    private val scaledTouchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop * 4f }
    private val dismissEdge by lazy { height * 0.3f }
    private var singleTouch = true
    private var lastX = 0f
    private var lastY = 0f
    private val listeners = mutableListOf<Listener>()
    private var clickListener: OnClickListener? = null
    private var longClickListener: OnLongClickListener? = null

    init {
        setOnTouchListener(this)
    }

    fun addListener(listener: Listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        clickListener = listener
    }

    override fun setOnLongClickListener(listener: OnLongClickListener?) {
        longClickListener = listener
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        handleDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    private fun handleDispatchTouchEvent(event: MotionEvent?) {
        if (!prepared) return

        when (event?.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                singleTouch = false
                animate()
                    .translationX(0f).translationY(0f).scaleX(1f).scaleY(1f)
                    .setDuration(200).start()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> up()
            MotionEvent.ACTION_MOVE -> {
                if (singleTouch) {
                    if (lastX == 0f) lastX = event.rawX
                    if (lastY == 0f) lastY = event.rawY
                    val offsetX = event.rawX - lastX
                    val offsetY = event.rawY - lastY
                    fakeDrag(offsetX, offsetY)
                }
            }
        }
    }

    private fun fakeDrag(offsetX: Float, offsetY: Float) {
        if (abs(offsetY) > scaledTouchSlop) {

            parent?.requestDisallowInterceptTouchEvent(true)
            val fraction = abs(max(-1f, min(1f, offsetY / height)))
            val fakeScale = 1 - min(0.4f, fraction)


            scaleX = fakeScale
            scaleY = fakeScale
            translationY = offsetY
            translationX = offsetX / 2
            listeners.toList().forEach { it.onDrag(this, fraction) }
        }
    }

    private fun up() {
        parent?.requestDisallowInterceptTouchEvent(false)
        singleTouch = true
        lastX = 0f
        lastY = 0f

        val offsetY = translationY
        val fraction = min(1f, offsetY / height)

        if (translationY < 0 || abs(translationY) <= dismissEdge) {
            listeners.toList().forEach { it.onRestore(this, fraction) }

            animate()
                .translationX(0f).translationY(0f).scaleX(1f).scaleY(1f)
                .setDuration(200).start()
        } else {
            listeners.toList().forEach { it.onRelease(this, fraction) }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animate().cancel()
    }

    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            // forward long click listener
            override fun onLongPress(e: MotionEvent) {
                longClickListener?.onLongClick(this@ExoVideoView2)
            }
        }).apply {
            setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                    clickListener?.onClick(this@ExoVideoView2)
                    return true
                }

                override fun onDoubleTapEvent(e: MotionEvent?) = false
                override fun onDoubleTap(e: MotionEvent?): Boolean = true
            })
        }
}