package com.benyq.guochat.ui.discover

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.benyq.guochat.R
import com.benyq.mvvm.ui.base.BaseDialogFragment
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.runOnUiThread
import com.benyq.mvvm.ext.textTrim
import kotlinx.android.synthetic.main.dialog_bottom_comment.*

/**
 * @author benyq
 * @time 2020/7/12
 * @e-mail 1520063035@qq.com
 * @note  底部评论界面
 */
class AddCircleCommentDialog : BaseDialogFragment(){

    companion object {
        fun newInstance(): AddCircleCommentDialog{
            return AddCircleCommentDialog()
        }
    }

    private var mConfirmAction: ((String)->Unit) ? = null

    override fun getLayoutId() = R.layout.dialog_bottom_comment

    override fun initView() {
        etContent.requestFocus()
        tvSend.setOnClickListener {
            val content = etContent.textTrim()
            mConfirmAction?.invoke(content)
            dismiss()
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
            it.setWindowAnimations(R.style.exist_menu_anim_style)
            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }
    }

    override fun onResume() {
        super.onResume()
        runOnUiThread{
            val inManager: InputMethodManager= etContent.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun setConfirmAction(action: (String)->Unit) {
        mConfirmAction = action
    }
}