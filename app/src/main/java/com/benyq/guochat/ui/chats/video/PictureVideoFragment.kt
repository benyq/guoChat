package com.benyq.guochat.ui.chats.video

import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.benyq.guochat.R
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.mvvm.ext.checkFullScreenPhone
import com.benyq.mvvm.ext.dip2px
import com.benyq.mvvm.ui.base.BaseFragment
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.fragment_picture_video.*

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class PictureVideoFragment : BaseFragment() {

    private val pictureVideoViewModel: PictureVideoViewModel by activityViewModels()
    private lateinit var mCaptureController: CaptureController

    @Volatile
    private var mStartTime: Long = 0

    override fun getLayoutId() = R.layout.fragment_picture_video

    override fun initView() {
        super.initView()

        glSurfaceView.setEGLContextClientVersion(2)
        mCaptureController = CaptureController(requireActivity(), glSurfaceView)
        lifecycle.addObserver(mCaptureController)
        resizeViewMargin()
    }

    override fun initListener() {

        ivClose.setOnClickListener {
            pictureVideoViewModel.clearAll()
        }

        ivCameraChange.setOnClickListener {
            mCaptureController.switchCamera()
        }

        with(captureView) {
            setPictureAction {

            }

            setVideoStartAction {

            }

            setVideoEndAction {

            }
        }
    }


    private fun resizeViewMargin() {

        if (mContext.checkFullScreenPhone()) {
            val topMargin = mContext.dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCameraChangeParam = ivCameraChange.layoutParams as FrameLayout.LayoutParams
            ivCameraChangeParam.topMargin = topMargin
            ivCameraChange.layoutParams = ivCameraChangeParam

            val ivCloseParam = ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            ivClose.layoutParams = ivCloseParam
        }
    }

}