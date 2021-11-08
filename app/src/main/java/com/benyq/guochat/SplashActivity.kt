package com.benyq.guochat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.ui.MainActivity
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by lazy { getViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        immersionBar {
            hideBar(BarHide.FLAG_HIDE_BAR)
        }
        super.onCreate(savedInstanceState)
        if (avoidLaunchHereAgain()) {
            return
        }

        if (ChatLocalStorage.existUser()) {
            //判断是否之前登录过
            viewModel.checkToken()
        } else {
            ARouter.getInstance().build(RouterPath.CHAT_LOGIN_PWD).navigation()
            finish()
        }

        viewModel.checkResult.observe(this) {
            if (it) {
                goToActivity<MainActivity>()
                finish()
            } else {
                ARouter.getInstance().build(RouterPath.CHAT_LOGIN_PWD).navigation()
                finish()
            }
        }
    }

    private fun avoidLaunchHereAgain(): Boolean {
        //避免从桌面启动程序后，会重新实例化入口类的activity
        if (!isTaskRoot) {
            val intent = intent
            if (intent != null) {
                val action = intent.action
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == action) {
                    finish()
                    return true
                }
            }
        }
        return false
    }
}
