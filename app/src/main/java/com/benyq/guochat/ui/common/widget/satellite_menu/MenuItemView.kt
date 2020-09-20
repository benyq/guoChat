package com.benyq.guochat.ui.common.widget.satellite_menu

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.benyq.mvvm.ext.dip2px

/**
 * @author benyq
 * @time 2020/6/11
 * @e-mail 1520063035@qq.com
 * @note
 */
class MenuItemView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr), OnMenuActionListener {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, menuItem: MenuItem) : this(context, null)

    private var mBtn: ImageButton? = null
    private var mLabel: TextView? = null

    private var mAlphaAnimation = true

    private val mGapSize = 4
    private val mTextSize = 14f

    private lateinit var menuItem: MenuItem

    init {
        orientation = VERTICAL

        mBtn = ImageButton(context)
        addView(mBtn)

        mLabel = TextView(context)
        val labelLp = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        labelLp.gravity = Gravity.CENTER_HORIZONTAL
        mLabel?.run {
            setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSize)
            layoutParams = labelLp
            addView(this)
        }
    }

    fun setMenuItem(menuItem: MenuItem){

        setOnClickListener(menuItem.itemClickListener)
        this.menuItem = menuItem

        mLabel?.run {
            setTextColor(ContextCompat.getColor(context, menuItem.textColor))
            text = menuItem.label
        }

        mBtn?.run {
            val params = layoutParams as LayoutParams
            params.width = context.dip2px(menuItem.diameter).toInt()
            params.height = context.dip2px(menuItem.diameter).toInt()
            params.gravity = Gravity.CENTER_HORIZONTAL
            params.bottomMargin = mGapSize
            val ovalShape = OvalShape()
            val shapeDrawable = ShapeDrawable(ovalShape)
            shapeDrawable.paint.color = ContextCompat.getColor(context, menuItem.bgColor)
            background = shapeDrawable
            setImageResource(menuItem.icon)
            isClickable = false
        }
    }

    fun disableAlphaAnimation() {
        mAlphaAnimation = false
        alpha = 1f
    }

    fun getMenuIcon() = menuItem.icon

    override fun onMenuClose() {
        if (mAlphaAnimation){
            animate().alpha(0f).setDuration(120).withEndAction {
                setOnClickListener(null)
            }
        }else{
            setOnClickListener(null)
        }
    }

    override fun onMenuOpen() {
        if (mAlphaAnimation){
            animate().alpha(1f).setDuration(120).withEndAction {
                setOnClickListener(menuItem.itemClickListener)
            }
        }else{
            setOnClickListener(menuItem.itemClickListener)
        }
    }
}
