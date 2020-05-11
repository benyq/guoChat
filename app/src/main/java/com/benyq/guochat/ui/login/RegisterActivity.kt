package com.benyq.guochat.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.R
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.mvvm.annotation.BindViewModel

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
}
