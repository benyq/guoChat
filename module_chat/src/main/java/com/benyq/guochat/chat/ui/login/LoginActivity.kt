package com.benyq.guochat.chat.ui.login

import android.app.Activity
import com.alibaba.android.arouter.facade.annotation.Route
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityLoginBinding
import com.benyq.guochat.chat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.RegisterBean
import com.benyq.guochat.chat.model.vm.LoginViewModel
import com.benyq.guochat.chat.ui.MainActivity
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.textTrim
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
@Route(path = RouterPath.CHAT_LOGIN)
class LoginActivity : LifecycleActivity<LoginViewModel, ActivityLoginBinding>() {

    override fun initVM(): LoginViewModel = getViewModel()

    override fun provideViewBinding() = ActivityLoginBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()
        //这边要判断，是否开启指纹登录
        if (FingerprintVerifyManager.canAuthenticate(this) && ChatLocalStorage.personConfig.fingerprintLogin) {
            goToActivity<FingerLoginActivity>(exitAnim = R.anim.normal_out)
            finish()
        }
    }

    override fun initListener() {
        super.initListener()
        binding.btnLogin.setOnClickListener {
            val username = binding.etUserName.textTrim()
            val password = binding.etPassword.textTrim()
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

        binding.btnRegister.setOnClickListener {
            SmartJump.from(this).startForResult(RegisterActivity::class.java, { code, data ->
                if (code == Activity.RESULT_OK && data != null) {
                    val registerData =
                        data.getParcelableExtra<RegisterBean>(IntentExtra.registerData)
                    registerData?.run {
                        binding.etUserName.setText(userName)
                        binding.etPassword.setText(registerData.pwd)
                        binding.btnLogin.performClick()
                    }
                }
            })
        }
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            mLoginResult.observe(this@LoginActivity){
                goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
                finish()
            }
            loadingType.observe(this@LoginActivity){
                if (it.isLoading) {
                    showLoading(it.isSuccess)
                } else {
                    hideLoading()
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out)
    }
}
