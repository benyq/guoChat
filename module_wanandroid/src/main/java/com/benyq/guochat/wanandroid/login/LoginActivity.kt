package com.benyq.guochat.wanandroid.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import com.benyq.guochat.wanandroid.model.vm.LoginViewModel
import com.benyq.guochat.wanandroid.ui.page.LoginPage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setStatusBarColor(Color.White)
            LoginPage({
                finish()
            })
        }
        viewModel.loginResult.observe(this) {
            val data = Intent()
            data.putExtra("value", "hello world")
            setResult(RESULT_OK, data)
            finish()
        }
    }
}