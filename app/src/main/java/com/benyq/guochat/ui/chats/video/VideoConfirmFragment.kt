package com.benyq.guochat.ui.chats.video

import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.R
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_picture_confirm.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class VideoConfirmFragment : BaseFragment(){

    private lateinit var pictureVideoViewModel: PictureVideoViewModel

    override fun getLayoutId() = R.layout.fragment_video_confirm

    override fun initView() {
        pictureVideoViewModel = ViewModelProvider(activity!!).get(PictureVideoViewModel::class.java)
    }

    override fun initListener() {
        ivClose.setOnClickListener {
            pictureVideoViewModel.clearTop()
        }

        btnFinished.setOnClickListener {

        }

    }
}