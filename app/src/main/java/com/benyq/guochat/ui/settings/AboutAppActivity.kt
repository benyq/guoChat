package com.benyq.guochat.ui.settings

import com.benyq.guochat.R
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.guochat.ui.common.WebViewActivity
import com.benyq.mvvm.ext.versionName
import kotlinx.android.synthetic.main.activity_about_app.*

/**
 * @author benyq
 * @time 2020/5/23
 * @e-mail 1520063035@qq.com
 * @note 关于APP的信息
 */
class AboutAppActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_about_app

    override fun initView() {
        val content = "版本号: ${versionName()}"
        tvVersionName.text = content
    }

    override fun initListener() {
        icAppUrl.setOnClickListener {
            WebViewActivity.gotoWeb(this, "https://github.com/benyq/guoChat", "果聊项目地址")
        }

        headerView.setBackAction { finish() }
    }
}
