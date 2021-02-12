package com.benyq.imageviewer.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.benyq.imageviewer.PreviewViewModel
import com.benyq.module_base.ext.loge
import com.github.chrisbanes.photoview.PhotoView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class PhotoView2 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : PhotoView(context, attrs, defStyleAttr){

    interface Listener {
        //拖动
        fun onDrag(view: PhotoView2, fraction: Float)
        //图片大小还原
        fun onRestore(view: PhotoView2, fraction: Float)
        //退出
        fun onRelease(view: PhotoView2, fraction: Float)

    }

    private var listener: Listener? = null

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    private val scaledTouchSlop by lazy { ViewConfiguration.get(context).scaledTouchSlop * 4f }
    private val mViewModel by lazy { (context as FragmentActivity?)?.let { ViewModelProvider(it).get(
        PreviewViewModel::class.java) }  }
    private val dismissEdge by lazy { height * 0.3f }

    private var singleTouch = true
    private var lastX = 0f
    private var lastY = 0f


    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        handleDispatchTouchEvent(event)
        return super.dispatchTouchEvent(event)
    }

    private fun handleDispatchTouchEvent(event: MotionEvent?) {
        when(event?.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                // 这个时候是有第二个或第三个手指点到了屏幕
                //需要还原图片
                setSingleTouch(false)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> up()
            MotionEvent.ACTION_MOVE -> {
                if (singleTouch && scale == 1f) {
                    //这个时候说明是单指操作，可以向下滑动图片缩小
                    if (lastX == 0f) lastX = event.rawX
                    if (lastY == 0f) lastY = event.rawY
                    drag(event.rawX - lastX, event.rawY - lastY)
                }
            }
        }
    }

  
    private fun drag(offsetX: Float, offsetY: Float) {

        //水平方向滑动足够大，才允许
        if (abs(offsetY) > scaledTouchSlop) {
            setAllowParentInterceptOnEdge(false)
            val fraction = abs(max(-1f, min(1f, offsetY / height)))
            val fakeScale = 1 - min(0.4f, fraction)

            scaleX = fakeScale
            scaleY = fakeScale
            translationY = offsetY
            translationX = offsetX / 2

            listener?.onDrag(this, fraction)
        }
    }

    private fun up() {
        setAllowParentInterceptOnEdge(true)
        lastX = 0f
        lastY = 0f
        setSingleTouch(true)


        val offsetY = translationY
        val fraction = min(1f, abs(offsetY) / height)

        if(scale < 1f){
            setScale(1f, true)
        }else if (translationY < 0 || abs(translationY) <= dismissEdge) {

            listener?.onRestore(this, fraction)

            animate().translationX(0f).translationY(0f).scaleX(1f).scaleY(1f)
                .setDuration(200).start()
        }else {
            //这个时候是需要退出的
            listener?.onRelease(this, fraction)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animate().cancel()
    }

    private fun setSingleTouch(value: Boolean) {
        singleTouch = value
        mViewModel?.setViewerUserInputEnabled(value)
    }

}