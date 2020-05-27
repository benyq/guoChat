package com.benyq.guochat.ui.common.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.IntDef
import androidx.annotation.RestrictTo
import androidx.core.graphics.drawable.DrawableCompat
import com.benyq.guochat.R
import com.benyq.mvvm.ext.getColorRef
import com.benyq.mvvm.ext.getDrawableRef
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

    companion object {
        const val toolbarTypeNormal = 1
        const val toolbarTypeDark = 2
    }

    private val titleStart = 1
    private val titleCenter = 2
    private val titleEnd = 3


    private var mTitle: String
    private var mTitleGravity: Int
    private var mToolbarType: Int

    /**
     * 是否显示返回按钮
     */
    private var mEnableBack = true
    private var mEnableMenu = false

    private var mMenuText: String
    private var mMenuBtnShow: Boolean
    private val headerViewBg: Drawable?


    init {
        View.inflate(context, R.layout.layout_common_toolbar, this)
        val array = getContext().obtainStyledAttributes(
            attrs,
            R.styleable.HeaderView
        )
        mTitle = array.getString(R.styleable.HeaderView_toolbar_title) ?: ""
        mMenuText = array.getString(R.styleable.HeaderView_toolbar_menu_text) ?: ""
        mEnableBack = array.getBoolean(R.styleable.HeaderView_enable_back, true)
        mEnableMenu = array.getBoolean(R.styleable.HeaderView_toolbar_menu, false)
        mMenuBtnShow = array.getBoolean(R.styleable.HeaderView_toolbar_menu_btn_show, false)
        mTitleGravity = array.getInt(R.styleable.HeaderView_title_gravity, titleCenter)
        mToolbarType = array.getInt(R.styleable.HeaderView_toolbar_type, toolbarTypeNormal)
        headerViewBg = array.getDrawable(R.styleable.HeaderView_toolbar_bg)


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

        setHeaderViewMode(mToolbarType)

        tvMenu.visibility = if (mMenuBtnShow) View.VISIBLE else View.GONE
        tvMenu.text = mMenuText
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

    fun setMenuBtnAction(action: View.() -> Unit) {
        tvMenu.visibility = View.VISIBLE
        tvMenu.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setMenuBtnEnable(enabled: Boolean) {
        if (enabled) {
            tvMenu.setBackgroundResource(R.drawable.button_selector)
            tvMenu.setTextColor(Color.WHITE)
        } else {
            tvMenu.setTextColor(context.getColorRef(R.color.gray))
            tvMenu.setBackgroundColor(context.getColorRef(R.color.color_3fa9a9a9))
        }
        tvMenu.isEnabled = enabled
    }

    fun setToolbarTitle(title: String) {
        toolbarTitle.text = title
    }

    fun setHeaderViewMode(mode: Int) {
        mToolbarType = mode
        if (toolbarTypeDark == mToolbarType) {
            toolbarTitle.setTextColor(Color.WHITE)
            toolbar.setBackgroundResource(R.color.color_2a2a2a)

            toolbarBack.setImageDrawable(
                tintDrawable(
                    context.getDrawableRef(R.drawable.ic_arrow_left_dark)!!,
                    ColorStateList.valueOf(Color.WHITE)
                )
            )
            toolbarMenu.setImageDrawable(
                tintDrawable(
                    context.getDrawableRef(R.drawable.ic_three_dots)!!,
                    ColorStateList.valueOf(Color.WHITE)
                )
            )

        } else {
            toolbarTitle.setTextColor(Color.BLACK)
            toolbar.setBackgroundResource(R.color.darkgrey)

            toolbarBack.setImageDrawable(
                tintDrawable(
                    context.getDrawableRef(R.drawable.ic_arrow_left_dark)!!,
                    ColorStateList.valueOf(Color.BLACK)
                )
            )
            toolbarMenu.setImageDrawable(
                tintDrawable(
                    context.getDrawableRef(R.drawable.ic_three_dots)!!,
                    ColorStateList.valueOf(Color.BLACK)
                )
            )
        }
        headerViewBg?.run {
            toolbar.background = this
        }
    }

    private fun tintDrawable(drawable: Drawable, colors: ColorStateList): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }


}