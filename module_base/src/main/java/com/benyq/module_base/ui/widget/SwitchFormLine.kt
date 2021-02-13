package com.benyq.module_base.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import androidx.core.content.ContextCompat
import com.benyq.module_base.R
import com.benyq.module_base.databinding.ViewPicturePuzzleBinding
import com.benyq.module_base.databinding.ViewSwitchLineBinding

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

    private var binding: ViewSwitchLineBinding = ViewSwitchLineBinding.inflate(
        LayoutInflater.from(context), this, true)

    init {
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
        binding.tvTitle.text = title
        binding.viewLine.visibility = if (showDivide) View.VISIBLE else View.GONE
        binding.tvTitle.setTextColor(lineTitleColor)
        binding.swContent.isChecked = isChecked

        binding.swContent.setOnCheckedChangeListener { buttonView, isChecked ->
            mOnCheckedAction?.invoke(buttonView, isChecked)
        }
    }


    fun setChecked(checked: Boolean){
        isChecked = checked
        binding.swContent.isChecked = checked
    }

    fun isChecked() = binding.swContent.isChecked

    fun getSwitchButton(): Switch = binding.swContent

    fun setOnCheckChangedAction(listener: (View, Boolean)->Unit){
        mOnCheckedAction = listener
    }

}