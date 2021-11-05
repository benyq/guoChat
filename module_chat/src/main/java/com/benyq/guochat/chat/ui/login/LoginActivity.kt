package com.benyq.guochat.chat.ui.login

import com.alibaba.android.arouter.facade.annotation.Route
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.ActivityLoginBinding
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
@Route(path = RouterPath.CHAT_LOGIN_PWD)
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun provideViewBinding() = ActivityLoginBinding.inflate(layoutInflater)


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
    }
}
