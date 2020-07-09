package com.benyq.guochat.ui.chats.video

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.ui.base.BaseFragment
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_picture_confirm.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note 拍完照片之后确认
 */

class PictureConfirmFragment : BaseFragment() {

    private val pictureVideoViewModel: PictureVideoViewModel by sharedViewModel()

    private lateinit var imgPath: String

    override fun getLayoutId() = R.layout.fragment_picture_confirm

    override fun initView() {
        val defaultImgPath = ""
        imgPath = arguments?.getString(IntentExtra.imgPath, defaultImgPath) ?: defaultImgPath

        Glide.with(this).load(imgPath).into(ivPicture)

    }

    override fun initListener() {
        ivClose.setOnClickListener {
            pictureVideoViewModel.clearTop()
        }

        btnFinished.setOnClickListener {
            pictureVideoViewModel.finishImg(imgPath)
        }

    }
}