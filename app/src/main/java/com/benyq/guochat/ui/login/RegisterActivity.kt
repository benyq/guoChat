package com.benyq.guochat.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.bean.RegisterBean
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.textTrim
import kotlinx.android.synthetic.main.activity_register.*

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class RegisterActivity : LifecycleActivity() {

    @BindViewModel
    lateinit var mViewModel: LoginViewModel
    
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
            mViewModel.register(userName, pwd)
        }
    }

    override fun dataObserver() {
        mViewModel.mRegisterResult.observe(this, Observer {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(IntentExtra.registerData, RegisterBean(etUserName.textTrim(), etPassword.textTrim()))
            })
            finish()
        })
    }
}
