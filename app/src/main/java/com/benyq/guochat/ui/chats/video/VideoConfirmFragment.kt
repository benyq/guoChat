package com.benyq.guochat.ui.chats.video

import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_picture_confirm.btnFinished
import kotlinx.android.synthetic.main.fragment_picture_confirm.ivClose
import kotlinx.android.synthetic.main.fragment_video_confirm.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class VideoConfirmFragment : BaseFragment(){

    private lateinit var videoVideoViewModel: PictureVideoViewModel

    private lateinit var videoPath: String

    override fun getLayoutId() = R.layout.fragment_video_confirm

    override fun initView() {
        videoVideoViewModel = ViewModelProvider(activity!!).get(PictureVideoViewModel::class.java)

        val defaultVideoPath = ""
        videoPath = arguments?.getString(IntentExtra.imgVideoPath, defaultVideoPath) ?: defaultVideoPath

        videoView.setVideoPath(videoPath)
        videoView.setOnCompletionListener {
            videoView.start()
        }
        videoView.setOnPreparedListener {
            videoView.start()
        }
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun initListener() {
        ivClose.setOnClickListener {
            videoVideoViewModel.clearTop()
        }

        btnFinished.setOnClickListener {
            videoVideoViewModel.finishVideo(videoPath)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.suspend()
    }

}