package com.benyq.guochat.ui.chats.video

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.databinding.FragmentPictureConfirmBinding
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.module_base.ext.checkFullScreenPhone
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ui.base.BaseFragment
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note 拍完照片之后确认
 */
@AndroidEntryPoint
class PictureConfirmFragment : BaseFragment<FragmentPictureConfirmBinding>() {

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()

    private lateinit var imgPath: String

    override fun provideViewBinding() = FragmentPictureConfirmBinding.inflate(layoutInflater)

    override fun initView() {
        val defaultImgPath = ""
        imgPath = arguments?.getString(IntentExtra.imgPath, defaultImgPath) ?: defaultImgPath

        Glide.with(this).load(imgPath).into(binding.ivPicture)

        resizeViewMargin()
    }

    override fun initListener() {
        binding.ivClose.setOnClickListener {
            File(imgPath).run {
                if (exists()) {
                    delete()
                }
            }
            pictureVideoViewModel.clearTop()
        }

        binding.btnFinished.setOnClickListener {
            pictureVideoViewModel.finishImg(imgPath)
        }

    }

    private fun resizeViewMargin() {

        if (mContext.checkFullScreenPhone()) {
            val topMargin = mContext.dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCloseParam = binding.ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            binding.ivClose.layoutParams = ivCloseParam
        }
    }
}