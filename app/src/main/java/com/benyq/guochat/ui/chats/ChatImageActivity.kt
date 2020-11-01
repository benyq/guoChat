package com.benyq.guochat.ui.chats

import android.os.Handler
import android.view.WindowManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.mvvm.ui.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_chat_image.*

/**
 * @author benyq
 * @time 2020/5/14
 * @e-mail 1520063035@qq.com
 * @note 展示图片
 */
class ChatImageActivity : BaseActivity() {

    override fun initWidows() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun getLayoutId() = R.layout.activity_chat_image

    override fun initView() {
        isSupportSwipeBack = false
        val imgPath: String? = intent.getStringExtra(IntentExtra.imgPath)
        if (imgPath.isNullOrEmpty()) {
            Handler().postDelayed({
                finish()
            }, 500)
        }
        Glide.with(this).load(imgPath).into(ivContent)
    }

    override fun initListener() {
        ivContent.setOnClickListener {
            finish()
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.alpha_scale_in, R.anim.alpha_scale_out)
    }

}
