package com.benyq.module_base.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import androidx.core.content.ContextCompat
import com.benyq.module_base.R
import kotlinx.android.synthetic.main.view_switch_line.view.*

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2019/11/11
 * description:
 */
class SwitchFormLine(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    private var title: String? = ""
    private var showDivide = true
    private var isChecked = false
    private val lineTitleColor: Int
    private var mOnCheckedAction: ((View, Boolean)->Unit)? = null


    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        View.inflate(context, R.layout.view_switch_line, this)
        orientation = VERTICAL
        isClickable = true

        val array = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.SwitchFormLine
        )
        title = array.getString(R.styleable.SwitchFormLine_sf_form_title)
        isChecked = array.getBoolean(R.styleable.SwitchFormLine_checked, false)

        showDivide = array.getBoolean(R.styleable.SwitchFormLine_sf_line_show_divide, true)

        lineTitleColor = array.getColor(
            R.styleable.SwitchFormLine_sf_form_title_color,
            ContextCompat.getColor(context, R.color.color3C4044)
        )


        array.recycle()
        initView()
    }

    private fun initView() {
        tvTitle.text = title
        viewLine.visibility = if (showDivide) View.VISIBLE else View.GONE
        tvTitle.setTextColor(lineTitleColor)
        swContent.isChecked = isChecked

        swContent.setOnCheckedChangeListener { buttonView, isChecked ->
            mOnCheckedAction?.invoke(buttonView, isChecked)
        }
    }


    fun setChecked(checked: Boolean){
        isChecked = checked
        swContent.isChecked = checked
    }

    fun isChecked() = swContent.isChecked

    fun getSwitchButton(): Switch = swContent

    fun setOnCheckChangedAction(listener: (View, Boolean)->Unit){
        mOnCheckedAction = listener
    }

}