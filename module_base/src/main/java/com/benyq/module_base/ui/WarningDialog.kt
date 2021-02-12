package com.benyq.module_base.ui

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import com.benyq.module_base.R
import com.benyq.module_base.ext.getScreenWidth
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.visible
import kotlinx.android.synthetic.main.view_warning_dialog.view.*

/**
 * @author benyq
 * @time 2020/3/2
 * @e-mail 1520063035@qq.com
 * @note
 */
object WarningDialog {

    private var title: CharSequence = "提示"
    private var content: CharSequence = ""
    private var confirmText = "确定"
    private var cancelText = "取消"
    private var operateText = "操作"

    private var titleVisible = true
    private var mCancelable = true

    private var dismissDelay = 2000L
    private var dialog: Dialog? = null
    private val mainHandler = Handler(Looper.getMainLooper())
    private var sureAction: (() -> Unit)? = null
    private var cancelAction: (() -> Unit)? = null
    private var operateAction: (() -> Unit)? = null
    private var confirmAction: (() -> Unit)? = null

    fun show(context: Context, count: Int, action: WarningDialog.() -> Unit) {
        reset()
        this.action()
        dialog = dialog?.let {
            if (it.context != context) {
                hide()
                Dialog(context, R.style.dialog_style)
            }
            it
        } ?: Dialog(context, R.style.dialog_style)

        val view =
            View.inflate(context, R.layout.view_warning_dialog, null)

        dialog?.run {
            setContentView(view)
            initView(view, count)
            setCancelable(mCancelable)
            window?.let {
                val lp = it.attributes
                if (count == 3) {
                    lp.width = context.getScreenWidth() * 3 / 4
                } else {
                    lp.width = context.getScreenWidth() * 2 / 3
                }
                lp.gravity = Gravity.CENTER
            }
            show()
            if (dismissDelay > 0) {
                mainHandler.postDelayed({
                    WarningDialog.hide()
                }, dismissDelay)
            }
        }

    }

    fun title(title: CharSequence) = WarningDialog.apply {
        WarningDialog.title = title
    }

    fun content(content: CharSequence) = WarningDialog.apply {
        WarningDialog.content = content
    }

    fun confirm(confirm: String) = WarningDialog.apply {
        confirmText = confirm
    }

    fun cancel(cancel: String) = WarningDialog.apply {
        cancelText = cancel
    }

    fun tvOperate(operate: String) = WarningDialog.apply {
        operateText = operate
    }

    fun onSure(block: () -> Unit) {
        sureAction = block
    }

    fun onCancel(block: () -> Unit) {
        cancelAction = block
    }

    fun onConfirm(block: () -> Unit) {
        confirmAction = block
    }

    fun onOperate(block: () -> Unit) {
        operateAction = block
    }

    fun dismissDelay(delay: Long) {
        dismissDelay = delay
    }

    private fun reset() {
        sureAction = null
        cancelAction = null
        operateAction = null
        confirmAction = null

        content = ""
        dismissDelay = 0L
    }

    private fun hide() {
        dialog?.run {
            if (isShowing) {
                dismiss()
                dialog = null
            }
        }
    }

    private fun initView(view: View, count: Int) {
        view.tvTitle.text = title
        if (titleVisible) {
            view.tvTitle.visible()
        } else {
            view.tvTitle.gone()
        }
        view.tvContent.text = content
        view.tvOperate.text =
            operateText
        when (count) {
            0 -> {
                view.llOperateDouble.gone()
                view.llOperateSingle.gone()
            }
            1 -> {
                view.llOperateDouble.gone()
                view.llOperateSingle.visible()
                view.tvConfirmCenter.text =
                    confirmText
                view.tvConfirmCenter.setOnClickListener {
                    hide()
                    confirmAction?.invoke()
                }
            }
            2 -> {
                view.llOperateDouble.visible()
                view.llOperateSingle.gone()
                view.tvOperate.gone()
                view.tvConfirmRight.text =
                    confirmText
                view.tvCancelLeft.text =
                    cancelText
                view.tvConfirmRight.setOnClickListener {
                    hide()
                    sureAction?.invoke()
                }
                view.tvCancelLeft.setOnClickListener {
                    hide()
                    cancelAction?.invoke()
                }
            }
            3 -> {
                view.llOperateDouble.visible()
                view.llOperateSingle.gone()
                view.tvConfirmRight.text =
                    confirmText
                view.tvCancelLeft.text =
                    cancelText
                view.tvConfirmRight.setOnClickListener {
                    hide()
                    sureAction?.invoke()
                }
                view.tvCancelLeft.setOnClickListener {
                    hide()
                    cancelAction?.invoke()
                }
                view.tvOperate.setOnClickListener {
                    hide()
                    operateAction?.invoke()
                }
            }
        }
    }


}