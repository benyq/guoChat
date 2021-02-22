package com.benyq.guochat

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.guochat.chat.ui.login.LoginActivity
import com.benyq.guochat.databinding.ActivitySplashBinding
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.BaseActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        immersionBar {
            hideBar(BarHide.FLAG_HIDE_BAR)
        }
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_splash)
        if (avoidLaunchHereAgain()) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1000)
            withContext(Dispatchers.Main) {
                //这边要判断，是否开启指纹登录
                ARouter.getInstance().build(RouterPath.CHAT_LOGIN).navigation()
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
