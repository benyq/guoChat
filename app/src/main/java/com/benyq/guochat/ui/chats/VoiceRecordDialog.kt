package com.benyq.guochat.ui.chats

import android.view.MotionEvent
import com.benyq.guochat.R
import com.benyq.mvvm.ui.base.BaseDialogFragment
import com.benyq.mvvm.DrawableBuilder
import com.benyq.mvvm.ext.*
import kotlinx.android.synthetic.main.fragment_voice_record.*

/**
 * @author benyq
 * @time 2020/4/27
 * @e-mail 1520063035@qq.com
 * @note
 */

class VoiceRecordDialog : BaseDialogFragment() {

    var isShowing = false
    private var mTouchY = -1.0f

    private var mConfirmAction: (()->Unit)? = null
    private var mCancelAction: (()->Unit)? = null

    override fun getLayoutId() = R.layout.fragment_voice_record

    override fun initView() {
        llContainer.background =
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
                    lottieAnimationView.visible()
                    ivRecordCancel.gone()
                    tvRecord.setText(R.string.release_finger_send)
                }else {
                    lottieAnimationView.gone()
                    ivRecordCancel.visible()
                    tvRecord.setText(R.string.move_up_cancel)
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