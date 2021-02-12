package com.benyq.module_base.ui

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.benyq.module_base.R
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.runOnUiThread
import com.benyq.module_base.ui.base.BaseDialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.dialog_picture_puzzle_confirm.*

/**
 * @author benyq
 * @time 2020/10/18
 * @e-mail 1520063035@qq.com
 * @note 拼图确认dialog
 */
class PicturePuzzleConfirmDialog : BaseDialogFragment() {

    companion object {
        private const val IMG_URL = "imgUrl"
        fun newInstance(imgUrl: String, successAction: (()->Unit)? = null): PicturePuzzleConfirmDialog {
            return PicturePuzzleConfirmDialog().apply {
                arguments = bundleOf(IMG_URL to imgUrl)
                mSuccessAction = successAction
            }
        }
    }

    private var mSuccessAction: (()->Unit)? = null

    override fun getLayoutId() = R.layout.dialog_picture_puzzle_confirm

    override fun initView() {
        ppView.setResultAction { bool, time ->
            if (bool) {
                Toasts.show("快如闪电, 只用时${time}秒")
                runOnUiThread(200) {
                    dismiss()
                    mSuccessAction?.invoke()
                }
            } else {
                Toasts.show("错了，兄弟")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let {
            val lp = it.attributes
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.attributes = lp
        }
        runOnUiThread(500) {
            val imgUrl = arguments?.getString(IMG_URL)
            loadBitmap(imgUrl)
        }
    }

    private fun loadBitmap(imgUrl: String?) {
        Glide.with(mContext).asBitmap().load(imgUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    ppView.setBitmap(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })
    }
}