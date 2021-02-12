package com.benyq.guochat.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.bean.RegisterBean
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.ext.textTrim
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class RegisterActivity : LifecycleActivity<LoginViewModel>() {

    override fun initVM(): LoginViewModel = getViewModel()

    override fun getLayoutId() = R.layout.activity_register

    override fun initView() {
    }

    override fun initListener() {
        headerView.run {
            setBackAction { finish() }
        }

        btnRegister.setOnClickListener {
            val userName = etUserName.textTrim()
            val pwd = etPassword.textTrim()
            viewModelGet().register(userName, pwd)
        }
    }

    override fun dataObserver() {
        viewModelGet().mRegisterResult.observe(this, Observer {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(
                    IntentExtra.registerData,
                    RegisterBean(etUserName.textTrim(), etPassword.textTrim())
                )
            })
            finish()
        })
    }
}
