package com.benyq.guochat.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.textTrim
import com.benyq.guochat.ui.MainActivity
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.startActivity
import com.benyq.mvvm.response.SharedType
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginActivity : LifecycleActivity() {

    @BindViewModel
    lateinit var mViewModel: LoginViewModel

    override fun getLayoutId() = R.layout.activity_login

    override fun initView() {
        super.initView()
    }

    override fun initListener() {
        super.initListener()
        btnLogin.setOnClickListener {
            val username = etUserName.textTrim()
            val password = etPassword.textTrim()
            if (username.isEmpty()) {
                Toasts.show(R.string.username_empty)
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toasts.show(R.string.password_empty)
                return@setOnClickListener
            }
//            showLoading("正在登录")
            mViewModel.login(username, password)
        }
        btnRegister.setOnClickListener {
            startActivity<RegisterActivity>()
        }
    }

    override fun dataObserver() {
        with(mViewModel) {
            mLoginResult.observe(this@LoginActivity, Observer {
                startActivity<MainActivity>()
                finish()
            })
        }
    }
}
