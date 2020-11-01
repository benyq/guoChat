package com.benyq.guochat.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.comic.ComicActivity
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.ui.login.FingerLoginActivity
import com.benyq.guochat.ui.login.LoginActivity
import com.benyq.mvvm.ext.fromP
import com.benyq.mvvm.ext.goToActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        immersionBar {
            hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (avoidLaunchHereAgain()) {
            return
        }
        //在全面屏手机上拉升开屏页图片
        if (fromP()) {
            val lp = window.attributes
            lp.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
        }
//        goToActivity<TestActivity>()
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
