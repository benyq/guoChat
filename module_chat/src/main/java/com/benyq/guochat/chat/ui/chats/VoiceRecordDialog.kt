package com.benyq.guochat.chat.ui.chats

import android.view.MotionEvent
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.FragmentVoiceRecordBinding
import com.benyq.module_base.ui.base.BaseDialogFragment
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.ext.*

/**
 * @author benyq
 * @time 2020/4/27
 * @e-mail 1520063035@qq.com
 * @note
 */

class VoiceRecordDialog : BaseDialogFragment<FragmentVoiceRecordBinding>() {

    var isShowing = false
    private var mTouchY = -1.0f

    private var mConfirmAction: (()->Unit)? = null
    private var mCancelAction: (()->Unit)? = null

    override fun provideViewBinding() = FragmentVoiceRecordBinding.inflate(layoutInflater)

    override fun initView() {
        binding.llContainer.background =
            DrawableBuilder(mContext).fill(mContext.getColorRef(R.color.color3C4044)).corner(10f).build()
    }

    override fun onStart() {
        super.onStart()
        dialog?.run {
            mContext.changeSize(this, 0.55f)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            window?.setDimAmount(0f)
        }
    }

    override fun onResume() {
        super.onResume()
        isShowing = true
    }

    override fun onPause() {
        super.onPause()
        isShowing = false
    }

    // 初始化
    fun setTouchY(touchY: Int) {
        mTouchY = touchY.toFloat()
    }

    fun setConfirmAction(action: ()->Unit) {
        mConfirmAction = action
    }

    fun setCancelAction(action: ()->Unit) {
        mCancelAction = action
    }

    /**
     * 处理按键
     */
    fun dispatchTouchEvent(ev: MotionEvent) {

        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                if (ev.y <= mContext.dip2px(20) + mTouchY && ev.y >= mTouchY - mContext.dip2px(20)) {
                    binding.lottieAnimationView.visible()
                    binding.ivRecordCancel.gone()
                    binding.tvRecord.setText(R.string.release_finger_send)
                }else {
                    binding.lottieAnimationView.gone()
                    binding.ivRecordCancel.visible()
                    binding.tvRecord.setText(R.string.move_up_cancel)
                }
            }
            MotionEvent.ACTION_UP -> {

                loge("event ${ev.x} -- ${ev.y} **** -- $mTouchY")

                if (ev.y <= mContext.dip2px(20) + mTouchY && ev.y >= mTouchY - mContext.dip2px(20)) {
                    //判定为发送语音
                    loge("判定为发送语音")
                    mConfirmAction?.invoke()
                }else {
                    mTouchY = -1f
                    loge("这是取消语音输入")
                    mCancelAction?.invoke()
                }
                dismiss()
            }

        }
    }
}