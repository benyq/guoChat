package com.benyq.guochat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.ui.login.FingerLoginActivity
import com.benyq.guochat.ui.login.LoginActivity
import com.benyq.guochat.ui.openeye.OpenEyeCommunityActivity
import com.benyq.mvvm.ext.goToActivity
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (avoidLaunchHereAgain()) {
            return
        }
        lifecycleScope.launch(Dispatchers.IO) {
            delay(1500)
            withContext(Dispatchers.Main) {
                //这边要判断，是否开启指纹登录
                if (FingerprintVerifyManager.canAuthenticate(this@SplashActivity) && LocalStorage.personConfig.fingerprintLogin) {
                    goToActivity<FingerLoginActivity>(exitAnim = R.anim.normal_out)
                } else {
                    goToActivity<LoginActivity>(exitAnim = R.anim.normal_out)
                }
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
