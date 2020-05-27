package com.benyq.guochat.ui.me

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.benyq.guochat.R
import com.benyq.guochat.dip2px
import com.benyq.guochat.function.other.GlideEngine
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.guochat.ui.base.BaseDialogFragment
import com.benyq.guochat.ui.base.DrawableBuilder
import com.benyq.mvvm.ext.getScreenWidth
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.dialog_avatar_menu.*

/**
 * @author benyq
 * @time 2020/5/21
 * @e-mail 1520063035@qq.com
 * @note 改头像功能 Dialog
 */
class AvatarMenuDialog : BaseDialogFragment(){

    /**
     * 图片地址回调
     */
    private var mActionSelect: ((String, View) -> Unit) ? = null

    override fun getLayoutId() = R.layout.dialog_avatar_menu

    override fun initView() {
        llAvatarMenu.background = DrawableBuilder(mContext)
            .cornerRadii(FloatArray(8){0f}.apply {
                set(0, dip2px(mContext, 10))
                set(1, dip2px(mContext, 10))
                set(2, dip2px(mContext, 10))
                set(3, dip2px(mContext, 10))
            })
            .fill(Color.WHITE)
            .build()
        tvSelect.setOnClickListener {
            dismiss()
            PictureSelector.create(mContext as Activity)
                .openGallery(PictureMimeType.ofAll())
                .loadImageEngine(GlideEngine)
                .forResult(object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: List<LocalMedia>) {
                        val res = result[0]
                        mActionSelect?.invoke(res.path, tvSelect)
                    }

                    override fun onCancel() {

                    }
                })
        }
        tvSaveToPhone.setOnClickListener {

        }

        tvCancel.setOnClickListener { dismiss() }
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

    fun setActionSelect(action: (String, View) -> Unit) {
        mActionSelect = action
    }
}