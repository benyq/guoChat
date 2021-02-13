package com.benyq.module_base.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.DrawableCompat
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.R
import com.benyq.module_base.databinding.LayoutCommonToolbarBinding
import com.benyq.module_base.ext.getColorRef
import com.benyq.module_base.ext.getDrawableRef

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
    private var mToolbarMenuSrc: Drawable
    private val mToolbarBackIcon: Drawable

    private val mBgMenuBtn: Drawable

    private var binding: LayoutCommonToolbarBinding = LayoutCommonToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        mBgMenuBtn = DrawableBuilder(context)
            .fill(context.getColorRef(R.color.color_3fa9a9a9))
            .corner(5f)
            .build()
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
        mToolbarMenuSrc = array.getDrawable(R.styleable.HeaderView_toolbar_menu_src)
            ?: context.getDrawableRef(R.drawable.ic_three_dots)!!

        mToolbarBackIcon = array.getDrawable(R.styleable.HeaderView_toolbar_back_icon)
            ?: context.getDrawableRef(R.drawable.ic_arrow_left_dark)!!

        array.recycle()

        initView()
    }

    private fun initView() {
        binding.toolbarTitle.text = mTitle

        binding.toolbarBack.visibility = if (mEnableBack) View.VISIBLE else View.INVISIBLE
        binding.toolbarMenu.visibility = if (mEnableMenu) View.VISIBLE else View.INVISIBLE
        binding.toolbarMenu.setImageDrawable(mToolbarMenuSrc)
        binding.toolbarBack.setImageDrawable(mToolbarBackIcon)
        binding.toolbarTitle.gravity = when (mTitleGravity) {
            titleEnd -> {
                Gravity.END or Gravity.CENTER_VERTICAL
            }
            titleCenter -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.START or Gravity.CENTER_VERTICAL
            }
        }

        setHeaderViewMode(mToolbarType)

        binding.tvMenu.visibility = if (mMenuBtnShow) View.VISIBLE else View.GONE
        binding.tvMenu.text = mMenuText
    }

    fun setBackAction(action: View.() -> Unit) {
        binding.toolbarBack.visibility = View.VISIBLE
        binding.toolbarBack.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setMenuAction(action: View.() -> Unit) {
        binding.toolbarMenu.visibility = View.VISIBLE
        binding.toolbarMenu.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setMenuBtnAction(action: View.() -> Unit) {
        binding.tvMenu.visibility = View.VISIBLE
        binding.tvMenu.setOnClickListener {
            action.invoke(it)
        }
    }

    fun setMenuBtnEnable(enabled: Boolean) {
        if (enabled) {
            binding.tvMenu.setBackgroundResource(R.drawable.button_selector)
            binding.tvMenu.setTextColor(Color.WHITE)
        } else {
            binding.tvMenu.setTextColor(context.getColorRef(R.color.gray))
            binding.tvMenu.background = mBgMenuBtn
        }
        binding.tvMenu.isEnabled = enabled
    }

    fun setToolbarTitle(title: String?) {
        binding.toolbarTitle.text = title
    }

    fun setMenuSrc(@DrawableRes resId: Int) {
        mToolbarMenuSrc = context.getDrawableRef(resId) ?: mToolbarMenuSrc
        if (toolbarTypeDark == mToolbarType) {
            binding.toolbarMenu.setImageDrawable(
                tintDrawable(
                    mToolbarMenuSrc,
                    ColorStateList.valueOf(Color.WHITE)
                )
            )
        } else {
            binding.toolbarMenu.setImageDrawable(
                tintDrawable(
                    mToolbarMenuSrc,
                    ColorStateList.valueOf(Color.BLACK)
                )
            )
        }
    }

    fun setHeaderViewMode(mode: Int) {
        mToolbarType = mode
        if (toolbarTypeDark == mToolbarType) {
            binding.toolbarTitle.setTextColor(Color.WHITE)
            binding.toolbar.setBackgroundResource(R.color.color_2a2a2a)

            binding.toolbarBack.setImageDrawable(
                tintDrawable(
                    mToolbarBackIcon,
                    ColorStateList.valueOf(Color.WHITE)
                )
            )
            binding.toolbarMenu.setImageDrawable(
                tintDrawable(
                    mToolbarMenuSrc,
                    ColorStateList.valueOf(Color.WHITE)
                )
            )

        } else {
            binding.toolbarTitle.setTextColor(Color.BLACK)
            binding.toolbar.setBackgroundResource(R.color.darkgrey)

            binding.toolbarBack.setImageDrawable(
                tintDrawable(
                    mToolbarBackIcon,
                    ColorStateList.valueOf(Color.BLACK)
                )
            )
            binding.toolbarMenu.setImageDrawable(
                tintDrawable(
                    mToolbarMenuSrc,
                    ColorStateList.valueOf(Color.BLACK)
                )
            )
        }
        headerViewBg?.run {
            binding.toolbar.background = this
        }
    }

    private fun tintDrawable(drawable: Drawable, colors: ColorStateList): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTintList(wrappedDrawable, colors)
        return wrappedDrawable
    }


}