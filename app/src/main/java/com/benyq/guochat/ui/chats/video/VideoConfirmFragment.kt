package com.benyq.guochat.ui.chats.video

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.imageviewer.widgets.video.ExoVideoView
import com.benyq.mvvm.ext.checkFullScreenPhone
import com.benyq.mvvm.ext.dip2px
import com.benyq.mvvm.ext.gone
import com.benyq.mvvm.ui.base.BaseFragment
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_picture_confirm.btnFinished
import kotlinx.android.synthetic.main.fragment_picture_confirm.ivClose
import kotlinx.android.synthetic.main.fragment_video_confirm.*
import java.io.File

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
        resizeViewMargin()
        val defaultVideoPath = ""
        videoPath =
            arguments?.getString(IntentExtra.videoPath, defaultVideoPath) ?: defaultVideoPath
        videoDuration = arguments?.getInt(IntentExtra.videoDuration, 0) ?: 0

        videoView.setVideoRenderedCallback(object : ExoVideoView.VideoRenderedListener {
            override fun onRendered(view: ExoVideoView) {

            }
        })
        videoView.prepare(videoPath)
    }

    override fun onResume() {
        super.onResume()
        videoView.resume()
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }

    override fun initListener() {
        ivClose.setOnClickListener {
            File(videoPath).run {
                if (exists()) {
                    delete()
                }
            }
            videoVideoViewModel.clearTop()
        }

        btnFinished.setOnClickListener {
            videoVideoViewModel.finishVideo(videoPath, videoDuration)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoView.release()
    }


    private fun resizeViewMargin() {

        if (mContext.checkFullScreenPhone()) {
            val topMargin = mContext.dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCloseParam = ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            ivClose.layoutParams = ivCloseParam
        }
    }
}