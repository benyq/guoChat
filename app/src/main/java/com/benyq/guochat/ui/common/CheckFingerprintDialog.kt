package com.benyq.guochat.ui.common

import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.benyq.guochat.R
import com.benyq.guochat.databinding.DialogCheckFingerprintBinding
import com.benyq.module_base.ui.base.BaseDialogFragment
import com.benyq.module_base.ext.getScreenWidth

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note 指纹识别提示对话框
 */
class CheckFingerprintDialog : BaseDialogFragment<DialogCheckFingerprintBinding>() {

    private var shakeAnim: TranslateAnimation? = null

    companion object {
        fun newInstance(): CheckFingerprintDialog {
            return CheckFingerprintDialog()
        }
    }

    override fun provideViewBinding() = DialogCheckFingerprintBinding.inflate(layoutInflater)

    override fun initView() {

        shakeAnim = TranslateAnimation(-25f, 25f, 0f, 0f).apply {
            duration = 100L
            repeatCount = 4
            repeatMode = Animation.REVERSE
        }

        binding.tvCancel.setOnClickListener {
            binding.tvMessage.setText(R.string.please_check_fingerprint)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tvMessage.clearAnimation()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val lp = it.attributes
            lp.width = (mContext.getScreenWidth() * 0.75f).toInt()
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = lp
        }
    }

    /**
     * 当 message 是 empty的时候表示识别失败，
     * 部位empty表示，需要稍后再试
     */
    fun verifyMessage(message: String) {
        if (dialog?.isShowing == true) {
            if (message.isEmpty()) {
                binding.tvMessage.setText(R.string.please_try_again)
                binding.tvMessage.startAnimation(shakeAnim)
            } else {
                binding.tvMessage.text = message
            }
        }
    }
}