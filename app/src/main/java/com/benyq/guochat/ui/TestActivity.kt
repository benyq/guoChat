package com.benyq.guochat.ui

import android.os.Handler
import android.view.WindowManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.mvvm.ext.fromP
import com.benyq.mvvm.ui.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity() {


    override fun isFullScreen() = true


    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }


    override fun initView() {
        val imgPath: String? = intent.getStringExtra(IntentExtra.imgPath)
        if (imgPath.isNullOrEmpty()) {
            Handler().postDelayed({
                finish()
            }, 500)
        }
        Glide.with(this).load(imgPath).into(img)

        if (fromP()) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
    }

    override fun initListener() {

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_stay, R.anim.alpha_scale_out)
    }

}