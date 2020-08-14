package com.benyq.guochat.ui.chats.video

import android.app.Activity
import com.benyq.guochat.R
import com.benyq.guochat.function.media.VideoCaptureManager
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.sharedViewModel
import com.benyq.guochat.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_picture_video.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class PictureVideoFragment : BaseFragment() {

    private lateinit var mVideoCaptureManager: VideoCaptureManager
    private val pictureVideoViewModel: PictureVideoViewModel = sharedViewModel()


    override fun getLayoutId() = R.layout.fragment_picture_video

    override fun initView() {
        super.initView()
        mVideoCaptureManager = VideoCaptureManager(mContext as Activity, textureView).apply {
            setPictureCapturedAction { imgPath ->
                pictureVideoViewModel.showPictureConfirm(imgPath)
            }
            setVideoCapturedAction { videoPath, duration ->
                pictureVideoViewModel.showVideoConfirm(videoPath, videoDuration = duration)
            }
        }
        lifecycle.addObserver(mVideoCaptureManager)
    }

    override fun initListener() {

        ivClose.setOnClickListener {
            pictureVideoViewModel.clearAll()
        }

        ivCameraChange.setOnClickListener {
            mVideoCaptureManager.exchangeCamera()
        }

        with(captureView) {
            setPictureAction {
                mVideoCaptureManager.takePic()
            }

            setVideoStartAction {
                mVideoCaptureManager.startRecordingVideo()
            }

            setVideoEndAction {
                mVideoCaptureManager.stopRecordingVideo()
            }
        }
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mVideoCaptureManager.restartPreview()
        }
    }

}