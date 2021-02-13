package com.benyq.guochat.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.databinding.ActivityRegisterBinding
import com.benyq.guochat.model.bean.RegisterBean
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.ext.textTrim
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class RegisterActivity : LifecycleActivity<LoginViewModel, ActivityRegisterBinding>() {

    override fun initVM(): LoginViewModel = getViewModel()

    override fun provideViewBinding() = ActivityRegisterBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initListener() {
        binding.headerView.run {
            setBackAction { finish() }
        }

        binding.btnRegister.setOnClickListener {
            val userName = binding.etUserName.textTrim()
            val pwd = binding.etPassword.textTrim()
            viewModelGet().register(userName, pwd)
        }
    }

    override fun dataObserver() {
        viewModelGet().mRegisterResult.observe(this, Observer {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    IntentExtra.registerData,
                    RegisterBean(binding.etUserName.textTrim(), binding.etPassword.textTrim())
                )
            })
            finish()
        })
    }
}
