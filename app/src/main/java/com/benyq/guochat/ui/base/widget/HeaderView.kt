package com.benyq.guochat.ui.base.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import com.benyq.guochat.R
import kotlinx.android.synthetic.main.layout_common_toolbar.view.*

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
class HeaderView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private val titleStart = 1
    private val titleCenter = 2
    private val titleEnd = 3

    private var mTitle: String
    private var mTitleGravity: Int

    /**
     * 是否显示返回按钮
     */
    private var mEnableBack = true
    private var mEnableMenu = false

    init {
        View.inflate(context, R.layout.layout_common_toolbar, this)
        val array = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.HeaderView
        )
        mTitle = array.getString(R.styleable.HeaderView_toolbar_title) ?: ""
        mEnableBack = array.getBoolean(R.styleable.HeaderView_enable_back, true)
        mEnableMenu = array.getBoolean(R.styleable.HeaderView_toolbar_menu, false)
        mTitleGravity = array.getInt(R.styleable.HeaderView_title_gravity, titleCenter)
        array.recycle()

        initView()
    }

    private fun initView() {

        toolbarTitle.text = mTitle

        toolbarBack.visibility = if (mEnableBack) View.VISIBLE else View.INVISIBLE
        toolbarMenu.visibility = if (mEnableMenu) View.VISIBLE else View.INVISIBLE

        toolbarTitle.gravity = when (mTitleGravity) {
            titleEnd -> {
                Gravity.END
            }
            titleCenter -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.START
            }
        }
    }

    fun setBackAction(action: View.() -> Unit) {
        toolbarBack.visibility = View.VISIBLE
        toolbarBack.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setMenuAction(action: View.() -> Unit) {
        toolbarMenu.visibility = View.VISIBLE
        toolbarMenu.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle.text = title
    }
}