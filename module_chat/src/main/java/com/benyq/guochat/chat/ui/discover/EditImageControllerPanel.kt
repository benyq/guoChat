package com.benyq.guochat.chat.ui.discover

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.ViewEditImageControllerPanelBinding
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ext.visible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlin.math.min

/**
 * @author benyqYe
 * date 2021/5/11
 * e-mail 1520063035@qq.com
 * description TODO
 */

enum class FilterType(val type: String) {
    ANAGLYPH("浮雕"),AGAINST_WORLD("镜世界"),GRAY("灰色世界"),NONE("无特效")
}

class EditImageControllerPanel(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val mPaintAdapter = PaintAdapter()
    private val mPaintColor = arrayListOf(
        Color.BLUE, Color.CYAN, Color.GREEN, Color.RED,
        Color.WHITE, Color.MAGENTA, Color.DKGRAY, Color.YELLOW
    )

    private val mFilterAdapter = FilterAdapter()
    private val mFilterTypes = arrayListOf(FilterType.NONE, FilterType.AGAINST_WORLD, FilterType.ANAGLYPH, FilterType.GRAY)

    private var mOnFilterAction: ((FilterType)->Unit)? = null
    private var mOnPaintAction: ((Int)->Unit)? = null
    private var mOnEditFinishedAction: (()->Unit)? = null

    private var binding: ViewEditImageControllerPanelBinding =
        ViewEditImageControllerPanelBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.rvPaints.adapter = mPaintAdapter
        (binding.rvPaints.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
        binding.rvPaints.adapter = mPaintAdapter
        binding.rvPaints.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.HORIZONTAL,
            false
        )
        mPaintAdapter.setOnItemClickListener { adapter, view, position ->
            mPaintAdapter.updateItemPosition(position)
            mOnPaintAction?.invoke(mPaintColor[position])
        }
        mPaintAdapter.setList(mPaintColor)

        binding.rvFilters.adapter = mFilterAdapter
        binding.rvFilters.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        mFilterAdapter.setList(mFilterTypes)
        mFilterAdapter.setOnItemClickListener { _, _, position ->
            mFilterAdapter.updateItemPosition(position)
            mOnFilterAction?.invoke(mFilterTypes[position])
        }

        binding.llGraffiti.setOnClickListener {
            if (!binding.rvPaints.isVisible) {
                binding.rvPaints.visible()
                binding.rvFilters.gone()
            }
        }
        binding.llFilters.setOnClickListener {
            if (!binding.rvFilters.isVisible) {
                binding.rvFilters.visible()
                binding.rvPaints.gone()

                mOnFilterAction?.invoke(mFilterTypes[mFilterAdapter.selectedPosition()])
            }
        }
        binding.btnFinished.setOnClickListener {
            mOnEditFinishedAction?.invoke()
        }
    }

    fun setFilterAction(action: (FilterType)->Unit) {
        mOnFilterAction = action
    }

    fun setPaintAction(action: (Int)->Unit) {
        mOnPaintAction = action
    }

    fun setEditFinishedAction(action: ()->Unit) {
        mOnEditFinishedAction = action
    }

    fun hasEdited() = mFilterAdapter.selectedPosition() != 0
}


class PaintAdapter : BaseQuickAdapter<@ColorInt Int, BaseViewHolder>(R.layout.item_paint_color) {

    private var selectedPosition = 0

    override fun convert(holder: BaseViewHolder, item: Int) {
        val paintView = holder.getView<PaintView>(R.id.paintView)
        paintView.color = item
        paintView.itemSelected = selectedPosition == holder.layoutPosition

    }

    fun updateItemPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position

        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

}

class FilterAdapter : BaseQuickAdapter<FilterType, BaseViewHolder>(R.layout.item_filter) {

    private var selectedPosition = 0

    override fun convert(holder: BaseViewHolder, item: FilterType) {
        holder.getView<TextView>(R.id.tvFilterName).run {
            text = item.type
            setBackgroundResource(if (selectedPosition == holder.layoutPosition) R.drawable.ic_shape_filter_selected else R.drawable.ic_shape_filter_normal)
        }
    }

    fun updateItemPosition(position: Int) {
        val oldPosition = selectedPosition
        selectedPosition = position

        notifyItemChanged(oldPosition)
        notifyItemChanged(position)
    }

    fun selectedPosition() = selectedPosition
}

class PaintView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    View(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    @ColorInt
    private var mColor: Int = Color.BLACK

    var color: Int
        get() = mColor
        set(value) {
            mColor = value
        }

    private val mBorderColor = Color.parseColor("#4687d7")

    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mColor
    }

    private var mViewWidth = 0f
    private var mViewHeight = 0f

    private var innerCircleRadius = 0f
    private var outerCircleRadius = 0f

    var itemSelected: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)

        val minMeasureSpec = MeasureSpec.makeMeasureSpec(min(width, height), MeasureSpec.EXACTLY)

        setMeasuredDimension(
            minMeasureSpec,
            minMeasureSpec
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w.toFloat()
        mViewHeight = h.toFloat()

        val stockWidth = mViewWidth / 12
        outerCircleRadius = mViewWidth / 2 - stockWidth / 2
        innerCircleRadius = mViewWidth * 4 / 10

        mPaint.strokeWidth = stockWidth

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let {
            it.translate(mViewWidth / 2, mViewHeight / 2)

            mPaint.color = mColor
            mPaint.style = Paint.Style.FILL_AND_STROKE
            it.drawCircle(0f, 0f, innerCircleRadius, mPaint)

            if (itemSelected) {
                mPaint.color = mBorderColor
                mPaint.style = Paint.Style.STROKE
                it.drawCircle(0f, 0f, outerCircleRadius, mPaint)
            }
        }
    }
}
