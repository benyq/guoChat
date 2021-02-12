package com.benyq.guochat.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.test.TestActivity
import com.benyq.guochat.ui.login.FingerLoginActivity
import com.benyq.guochat.ui.login.LoginActivity
import com.benyq.module_base.ext.fromP
import com.benyq.module_base.ext.goToActivity
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
        setContentView(R.layout.activity_splash)
        if (avoidLaunchHereAgain()) {
            return
        }
        val test : Boolean = false
        if (test) {
            goToActivity<TestActivity>()
        }else {
            lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    //这边要判断，是否开启指纹登录
                    if (FingerprintVerifyManager.canAuthenticate(this@SplashActivity) && ChatLocalStorage.personConfig.fingerprintLogin) {
                        goToActivity<FingerLoginActivity>(exitAnim = R.anim.normal_out)
                    } else {
                        goToActivity<LoginActivity>(exitAnim = R.anim.normal_out)
                    }
                    finish()
                }
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
