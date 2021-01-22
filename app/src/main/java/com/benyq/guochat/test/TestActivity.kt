package com.benyq.guochat.test

import android.widget.FrameLayout
import com.benyq.guochat.databinding.ActivityTestBinding
import com.benyq.guochat.function.video.CaptureController
import com.benyq.guochat.function.video.filter.FilterFactory
import com.benyq.guochat.function.video.filter.FilterType
import com.benyq.mvvm.ext.binding
import com.benyq.mvvm.ext.checkFullScreen
import com.benyq.mvvm.ext.dip2px
import com.benyq.mvvm.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar

class TestActivity : BaseActivity() {

    private val mBinding: ActivityTestBinding by binding()
    private val mCaptureController by lazy { CaptureController(this, mBinding.glSurfaceView) }

    override fun isHideBar() = true

    override fun getLayoutView() = mBinding.root

    override fun initView() {
        lifecycle.addObserver(mCaptureController)
        resizeViewMargin()
    }

    var hasFilter = false
    override fun initListener() {
        mBinding.captureView.setPictureAction {

        }
        mBinding.captureView.setVideoStartAction {

        }
        mBinding.captureView.setVideoEndAction {

        }

        mBinding.ivCameraChange.setOnClickListener {
            mCaptureController.switchCamera()
        }

        mBinding.btnAddFilter.setOnClickListener {
            if (!hasFilter) {
                mCaptureController.updateFilter(FilterFactory.createFilter(FilterType.FILTER_CARTOON))
            }else {
                mCaptureController.removeFilter()
            }
            hasFilter = !hasFilter
        }
    }


    private fun resizeViewMargin() {

        if (checkFullScreen()) {
            val topMargin = dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCameraChangeParam =
                mBinding.ivCameraChange.layoutParams as FrameLayout.LayoutParams
            ivCameraChangeParam.topMargin = topMargin
            mBinding.ivCameraChange.layoutParams = ivCameraChangeParam

            val ivCloseParam = mBinding.ivClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            mBinding.ivClose.layoutParams = ivCloseParam
        }
    }

}