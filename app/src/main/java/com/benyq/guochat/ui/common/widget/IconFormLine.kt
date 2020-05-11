package com.benyq.guochat.ui.common.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.benyq.guochat.R
import kotlinx.android.synthetic.main.view_icon_form_line.view.*

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2019/11/11
 * description:
 */
class IconFormLine(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {


    private var title: String? = ""
    private var content: String? = ""
    private var showDivide = true
    private var showLineArrow = true

    private val lineTitleColor: Int
    private val lineContentColor: Int
    private val formIconBg: Drawable?

    private val imgRes: Int

    private var hint: String? = ""

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        View.inflate(context, R.layout.view_icon_form_line, this)
        orientation = VERTICAL
        isClickable = true

        val array = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.FormLine
        )
        title = array.getString(R.styleable.FormLine_form_title)

        content = array.getString(R.styleable.FormLine_form_content)

        showDivide = array.getBoolean(R.styleable.FormLine_line_show_divide, true)
        showLineArrow = array.getBoolean(R.styleable.FormLine_line_show_arrow, true)

        hint = array.getString(R.styleable.FormLine_et_hint)

        lineTitleColor = array.getColor(
            R.styleable.FormLine_form_title_color,
            ContextCompat.getColor(context, R.color.color3C4044)
        )

        formIconBg = array.getDrawable(
            R.styleable.FormLine_form_icon_bg
        )

        lineContentColor =
            array.getColor(
                R.styleable.FormLine_form_content_color,
                ContextCompat.getColor(context, R.color.color3C4044)
            )

        imgRes = array.getResourceId(R.styleable.FormLine_form_icon, R.color.color3C4044)

        array.recycle()
        initView()
    }

    private fun initView() {
        tvTitle.text = title
        tvContent.text = content
        tvContent.hint = hint
        viewLine.visibility = if (showDivide) View.VISIBLE else View.GONE
        ivLineArrow.visibility = if (showLineArrow) View.VISIBLE else View.GONE
        tvTitle.setTextColor(lineTitleColor)
        tvContent.setTextColor(lineContentColor)
        ivImg.setImageResource(imgRes)
        ivImg.background = formIconBg
    }


    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setContent(content: String) {
        tvContent.text = content
        tvContent.setTextColor(ContextCompat.getColor(context, R.color.color3C4044))
    }

    fun setRedContent(content: String) {
        tvContent.text = content
        tvContent.setTextColor(ContextCompat.getColor(context, R.color.red))
    }

    fun getContent(): String {
        return tvContent.text.toString().trim()
    }

}