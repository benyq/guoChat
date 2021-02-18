package com.benyq.guochat.chat.ui.chats.video

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.FragmentVideoConfirmBinding
import com.benyq.guochat.chat.model.vm.PictureVideoViewModel
import com.benyq.imageviewer.widgets.video.ExoVideoView
import com.benyq.module_base.ext.checkFullScreenPhone
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ui.base.BaseFragment
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class VideoConfirmFragment : BaseFragment<FragmentVideoConfirmBinding>() {

    private val videoVideoViewModel: PictureVideoViewModel by activityViewModels()

    private lateinit var videoPath: String
    private var videoDuration: Int = 0

    override fun provideViewBinding() = FragmentVideoConfirmBinding.inflate(layoutInflater)

    override fun initView() {
        resizeViewMargin()
        val defaultVideoPath = ""
        videoPath =
            arguments?.getString(IntentExtra.videoPath, defaultVideoPath) ?: defaultVideoPath
        videoDuration = arguments?.getInt(IntentExtra.videoDuration, 0) ?: 0

        binding.videoView.setVideoRenderedCallback(object : ExoVideoView.VideoRenderedListener {
            override fun onRendered(view: ExoVideoView) {

            }
        })
        binding.videoView.prepare(videoPath)
    }

    override fun onResume() {
        super.onResume()
        binding.videoView.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun initListener() {
        binding.ivClose.setOnClickListener {
            File(videoPath).run {
                if (exists()) {
                    delete()
                }
            }
            videoVideoViewModel.clearTop()
        }

        binding.btnFinished.setOnClickListener {
            videoVideoViewModel.finishVideo(videoPath, videoDuration)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.release()
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