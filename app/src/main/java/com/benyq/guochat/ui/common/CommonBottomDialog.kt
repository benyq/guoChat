package com.benyq.guochat.ui.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import com.benyq.guochat.R
import com.benyq.guochat.dip2px
import com.benyq.guochat.ui.base.BaseDialogFragment
import com.benyq.mvvm.ext.getColorRef
import com.benyq.mvvm.ext.getScreenWidth
import kotlinx.android.synthetic.main.dialog_common_bottom_menu.*

/**
 * @author benyq
 * @time 2020/5/25
 * @e-mail 1520063035@qq.com
 * @note 通用底部弹出MenuDialog
 */
class CommonBottomDialog : BaseDialogFragment() {

    companion object {
        private const val titleKey = "titleKey"
        fun newInstance(titles: Array<String>): CommonBottomDialog {
            return CommonBottomDialog().apply {
                arguments = bundleOf(titleKey to titles)
            }
        }
    }

    private var mOnMenuAction: ((View, Int) -> Unit)? = null
    private lateinit var mTitles: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mTitles = arguments?.getStringArray(titleKey) ?: arrayOf()
    }

    override fun getLayoutId() = R.layout.dialog_common_bottom_menu

    override fun initView() {
        tvCancel.setOnClickListener { dismiss() }
        mTitles.forEachIndexed { index, s ->
            val textView = TextView(mContext)
            textView.gravity = Gravity.CENTER
            textView.setPadding(dip2px(mContext, 15).toInt())
            textView.text = s
            textView.textSize = 14f
            textView.setTextColor(mContext.getColorRef(R.color.color_2a2a2a))
            textView.setOnClickListener {
                mOnMenuAction?.invoke(it, index)
                dismiss()
            }
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            llContainer.addView(textView, layoutParams)
            if (index != mTitles.size - 1) {
                val view = View(mContext)
                val viewParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    dip2px(mContext, 0.3f).toInt()
                )
                view.setBackgroundResource(R.color.gray)
                llContainer.addView(view, viewParams)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val lp = it.attributes
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            lp.width = mContext.getScreenWidth()
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.BOTTOM
            it.attributes = lp
            it.setWindowAnimations(R.style.exist_menu_animstyle)
        }
    }

    fun setOnMenuAction(action: (View, Int) -> Unit) {
        mOnMenuAction = action
    }
}