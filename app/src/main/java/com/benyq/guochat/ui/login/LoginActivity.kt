package com.benyq.guochat.ui.login

import android.app.Activity
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.bean.RegisterBean
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.MainActivity
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.textTrim
import com.benyq.mvvm.response.SharedType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class LoginActivity : LifecycleActivity<LoginViewModel>() {

    override fun initVM(): LoginViewModel = getViewModel()

    override fun getLayoutId() = R.layout.activity_login

    override fun initView() {
        isSupportSwipeBack = false
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
            viewModelGet().login(username, password)
        }

        btnRegister.setOnClickListener {
            SmartJump.from(this).startForResult(RegisterActivity::class.java, { code, data ->
                if (code == Activity.RESULT_OK && data != null) {
                    val registerData =
                        data.getParcelableExtra<RegisterBean>(IntentExtra.registerData)
                    registerData?.run {
                        etUserName.setText(userName)
                        etPassword.setText(registerData.pwd)
                        btnLogin.performClick()
                    }
                }
            })
        }
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            mLoginResult.observe(this@LoginActivity, Observer {
                goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
                finish()
            })
            mSharedData.observe(this@LoginActivity, Observer {
                if (it.type == SharedType.SHOW_LOADING) {
                    showLoading(it.msg)
                } else if (it.type == SharedType.HIDE_LOADING) {
                    hideLoading()
                }
            })
        }
    }
}
