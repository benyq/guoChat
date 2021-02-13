package com.benyq.module_base.ui

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import com.benyq.module_base.R
import com.benyq.module_base.databinding.ViewWarningDialogBinding
import com.benyq.module_base.ext.getScreenWidth
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.visible

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



        dialog?.run {
            val binding = ViewWarningDialogBinding.inflate(layoutInflater)
            setContentView(binding.root)
            initView(binding, count)
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

    private fun initView(binding: ViewWarningDialogBinding, count: Int) {
        binding.tvTitle.text = title
        if (titleVisible) {
            binding.tvTitle.visible()
        } else {
            binding.tvTitle.gone()
        }
        binding.tvContent.text = content
        binding.tvOperate.text =
            operateText
        when (count) {
            0 -> {
                binding.llOperateDouble.gone()
                binding.llOperateSingle.gone()
            }
            1 -> {
                binding.llOperateDouble.gone()
                binding.llOperateSingle.visible()
                binding.tvConfirmCenter.text =
                    confirmText
                binding.tvConfirmCenter.setOnClickListener {
                    hide()
                    confirmAction?.invoke()
                }
            }
            2 -> {
                binding.llOperateDouble.visible()
                binding.llOperateSingle.gone()
                binding.tvOperate.gone()
                binding.tvConfirmRight.text =
                    confirmText
                binding.tvCancelLeft.text =
                    cancelText
                binding.tvConfirmRight.setOnClickListener {
                    hide()
                    sureAction?.invoke()
                }
                binding.tvCancelLeft.setOnClickListener {
                    hide()
                    cancelAction?.invoke()
                }
            }
            3 -> {
                binding.llOperateDouble.visible()
                binding.llOperateSingle.gone()
                binding.tvConfirmRight.text =
                    confirmText
                binding.tvCancelLeft.text =
                    cancelText
                binding.tvConfirmRight.setOnClickListener {
                    hide()
                    sureAction?.invoke()
                }
                binding.tvCancelLeft.setOnClickListener {
                    hide()
                    cancelAction?.invoke()
                }
                binding.tvOperate.setOnClickListener {
                    hide()
                    operateAction?.invoke()
                }
            }
        }
    }


}