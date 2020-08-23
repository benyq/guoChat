package com.benyq.guochat.ui.chats.video

import androidx.fragment.app.activityViewModels
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_picture_confirm.btnFinished
import kotlinx.android.synthetic.main.fragment_picture_confirm.ivClose
import kotlinx.android.synthetic.main.fragment_video_confirm.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class VideoConfirmFragment : BaseFragment() {

    private val videoVideoViewModel: PictureVideoViewModel by activityViewModels()

    private lateinit var videoPath: String
    private var videoDuration: Int = 0

    override fun getLayoutId() = R.layout.fragment_video_confirm

    override fun initView() {

        val defaultVideoPath = ""
        videoPath =
            arguments?.getString(IntentExtra.videoPath, defaultVideoPath) ?: defaultVideoPath
        videoDuration = arguments?.getInt(IntentExtra.videoDuration, 0) ?: 0

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
            videoVideoViewModel.finishVideo(videoPath, videoDuration)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.suspend()
    }

}