package com.benyq.guochat.ui.chats.video

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.mvvm.ext.checkFullScreen
import com.benyq.mvvm.ext.dip2px
import com.benyq.mvvm.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_picture_confirm.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note 拍完照片之后确认
 */
@AndroidEntryPoint
class PictureConfirmFragment : BaseFragment() {

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()

    private lateinit var imgPath: String

    override fun getLayoutId() = R.layout.fragment_picture_confirm

    override fun initView() {
        val defaultImgPath = ""
        imgPath = arguments?.getString(IntentExtra.imgPath, defaultImgPath) ?: defaultImgPath

        Glide.with(this).load(imgPath).into(ivPicture)

        resizeViewMargin()
    }

    override fun initListener() {
        ivClose.setOnClickListener {
            pictureVideoViewModel.clearTop()
        }

        btnFinished.setOnClickListener {
            pictureVideoViewModel.finishImg(imgPath)
        }

    }

    private fun resizeViewMargin() {

        if (mContext.checkFullScreen()) {
            val topMargin = mContext.dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCloseParam = ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            ivClose.layoutParams = ivCloseParam
        }
    }
}