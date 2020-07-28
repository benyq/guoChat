package com.benyq.guochat.ui.common.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.view.setPadding
import com.benyq.guochat.R
import com.benyq.guochat.dip2px
import com.benyq.mvvm.ext.getColorRef
import com.benyq.mvvm.ext.loge
import com.bumptech.glide.Glide
import kotlin.math.max
import kotlin.math.min

/**
 * @author benyq
 * @time 2020/6/20
 * @e-mail 1520063035@qq.com
 * @note  9宫格图片
 */
class NineGridLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    ViewGroup(context, attrs, defStyleAttr) {

    // attrs
    private var mEnableAdd: Boolean = false


    private val mPhotoUrls = mutableListOf<String>()
    private val mPhotoImageViews = mutableListOf<ImageView>()
    private val mAddButton: ImageButton = ImageButton(context)

    private val rowCount = 3

    private var mItemAction : ((View, List<String>, Int)->Unit)? = null

    /**
     * 图片之间间距
     */
    private var mGap: Int = dip2px(context, 5).toInt()

    init {

        val array = context.obtainStyledAttributes(
            attrs,
            R.styleable.NineGridLayout
        )
        mEnableAdd = array.getBoolean(R.styleable.NineGridLayout_ng_enable_add, true)
        array.recycle()

        mAddButton.background = ColorDrawable(Color.parseColor("#cdcdcd"))
        mAddButton.setImageResource(R.drawable.ic_photo_add)
        addView(mAddButton)
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    /**
     * 不滑动就可以返回false
     */
    override fun shouldDelayChildPressedState() = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val oldWidth = MeasureSpec.getSize(widthMeasureSpec)
        mGap = oldWidth / 40
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec) - 2 * mGap
        val itemWidth = width / rowCount
        var totalHeight = 0
        val childMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY)
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(childMeasureSpec, childMeasureSpec)
        }
        var childViewCount = childCount
        if (!mEnableAdd || childViewCount == 10) {
            //不显示AddButton
            childViewCount -= 1
        }
        val lineCount = (childViewCount + rowCount - 1) / rowCount
        totalHeight = lineCount * itemWidth + (lineCount - 1) * mGap
        setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(totalHeight, heightMode))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
        var startX = 0
        var startY = 0

        for (i in 1 until childCount) {
            val child = getChildAt(i)
            if (child is ImageButton) {
                continue
            }
            child.layout(
                startX,
                startY,
                startX + child.measuredWidth,
                startY + child.measuredHeight
            )
            startX += (child.measuredWidth + mGap)

            if (i % rowCount == 0) {
                startX = 0
                startY += (child.measuredHeight + mGap)
            }
        }
        if (childCount < 10 && mEnableAdd) {
            loge("startX  $startX   startY   $startY")
            mAddButton.layout(
                startX,
                startY,
                startX + mAddButton.measuredWidth,
                startY + mAddButton.measuredHeight
            )
        }
        showPhoto()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        loge("onInterceptTouchEvent ${ev?.action}")
        return super.onInterceptTouchEvent(ev)
    }

    fun addItem(url: String) {
        mPhotoUrls.add(url)
        val imageView = createImageView(mPhotoUrls.size - 1)
        mPhotoImageViews.add(imageView)
        addView(imageView)
    }

    fun addItems(urls: List<String>){
        removeAllItems()
        urls.forEach {
            addItem(it)
        }
    }

    fun getPhotoUrls(): List<String> = mPhotoUrls

    private fun removeAllItems(){
        mPhotoUrls.clear()
        mPhotoImageViews.forEach {
            if (indexOfChild(it) != -1){
                removeView(it)
            }
        }
        mPhotoImageViews.clear()
    }

    fun setAddAction(action: (View)->Unit){
        mAddButton.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setItemAction(action: (View, List<String>, Int)->Unit){
        mItemAction = action
    }

    private fun createImageView(position : Int): ImageView {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setOnClickListener {
            mItemAction?.invoke(it, mPhotoUrls, position)
        }
        return imageView
    }

    private fun showPhoto() {
        val count = min(mPhotoImageViews.size, mPhotoUrls.size)
        for (i in 0 until count) {
            Glide.with(context).load(mPhotoUrls[i]).into(mPhotoImageViews[i])
        }
    }
}