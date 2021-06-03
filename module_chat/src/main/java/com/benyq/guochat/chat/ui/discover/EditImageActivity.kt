package com.benyq.guochat.chat.ui.discover

import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityEditImageBinding
import com.benyq.module_base.ext.checkFullScreenPhone
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.loadImage
import com.benyq.module_base.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar

/**
 * @author benyqYe
 * date 2021/5/11
 * e-mail 1520063035@qq.com
 * description 图片编辑
 */

class EditImageActivity : BaseActivity<ActivityEditImageBinding>() {

    override fun isHideBar() = true

    private lateinit var mFilePath: String

    override fun provideViewBinding() = ActivityEditImageBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()

        binding.headerView.setBackAction {
            finish()
        }
        mFilePath = intent.getStringExtra(IntentExtra.editImagePath)!!

        binding.ivImage.loadImage(mFilePath)

        resizeViewMargin()
    }


    private fun resizeViewMargin() {
        if (this.checkFullScreenPhone()) {
            val topMargin = (this.dip2px(15) + ImmersionBar.getStatusBarHeight(this)).toInt()
            val headViewParam = binding.headerView.layoutParams as ConstraintLayout.LayoutParams
            headViewParam.topMargin = topMargin
            binding.headerView.layoutParams = headViewParam
        }
    }

}