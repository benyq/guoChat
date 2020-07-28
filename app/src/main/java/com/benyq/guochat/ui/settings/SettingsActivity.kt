package com.benyq.guochat.ui.settings

import com.benyq.guochat.R
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.mvvm.ext.goToActivity
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note 总的设置界面
 */
class SettingsActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_settings

    override fun initListener() {
        headerView.setBackAction { finish() }

        ifAboutApp.setOnClickListener {
            goToActivity<AboutAppActivity>()
        }
    }
}
