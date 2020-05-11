package com.benyq.guochat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.benyq.guochat.R
import com.benyq.guochat.asyncWithLifecycle
import com.benyq.guochat.then
import com.benyq.guochat.ui.login.LoginActivity
import com.benyq.mvvm.ext.startActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (avoidLaunchHereAgain()) {
            return
        }

        GlobalScope.asyncWithLifecycle(this) {
            delay(1500)
        }.then {
            startActivity<LoginActivity>()
            finish()
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
