package com.benyq.guochat.ui.login

import android.Manifest
import android.app.Activity
import android.os.Environment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.aspect.PermissionCheck
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.model.bean.RegisterBean
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.MainActivity
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.textTrim
import com.benyq.mvvm.response.SharedType
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.buffer
import okio.sink
import okio.source
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.File

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
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
            mViewModel.login(username, password)
        }

        btnRegister.setOnClickListener {
            login()
//            SmartJump.from(this).startForResult(RegisterActivity::class.java) { code, data ->
//                if (code == Activity.RESULT_OK && data != null) {
//                    val registerData =
//                        data.getParcelableExtra<RegisterBean>(IntentExtra.registerData)
//                    registerData?.run {
//                        etUserName.setText(userName)
//                        etPassword.setText(registerData.pwd)
//                        btnLogin.performClick()
//                    }
//                }
//            }
        }
    }

    override fun dataObserver() {
        with(mViewModel) {
            mLoginResult.observe(this@LoginActivity, Observer {
                goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
//                startActivity<TestActivity>()
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

    @PermissionCheck(checkString = [Manifest.permission.WRITE_EXTERNAL_STORAGE])
    private fun login() {
        lifecycleScope.launch {
            val futureTarget = Glide.with(this@LoginActivity)
                .asFile()
                .load(LocalStorage.userAccount.avatarUrl)
                .submit()

            val file: File = futureTarget.get()
            val parentPath = Environment.getExternalStoragePublicDirectory("avatar")
            if (!parentPath.exists()) {
                parentPath.mkdir()
            }

            val targetFile = File(parentPath, "ddddddd.png")

            val bufferedSource = file.source().buffer()
            val bufferedSink = targetFile.sink().buffer()
            bufferedSink.writeAll(bufferedSource)
            bufferedSink.close()
            bufferedSource.close()
            Toasts.show("存储成功")
        }
    }
}
