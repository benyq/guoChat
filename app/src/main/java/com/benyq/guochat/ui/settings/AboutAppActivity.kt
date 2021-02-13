package com.benyq.guochat.ui.settings

import com.benyq.guochat.R
import com.benyq.guochat.databinding.ActivityAboutAppBinding
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.module_base.ui.WebViewActivity
import com.benyq.module_base.ext.versionName

/**
 * @author benyq
 * @time 2020/5/23
 * @e-mail 1520063035@qq.com
 * @note 关于APP的信息
 */
class AboutAppActivity : BaseActivity<ActivityAboutAppBinding>() {

    override fun provideViewBinding() = ActivityAboutAppBinding.inflate(layoutInflater)

    override fun initView() {
        val content = "版本号: ${versionName()}"
        binding.tvVersionName.text = content
    }

    override fun initListener() {
        binding.icAppUrl.setOnClickListener {
            WebViewActivity.gotoWeb(this, "https://github.com/benyq/guoChat", "果聊项目地址")
        }

        binding.headerView.setBackAction { finish() }
    }
}
