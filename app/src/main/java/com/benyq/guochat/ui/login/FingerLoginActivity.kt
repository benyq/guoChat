package com.benyq.guochat.ui.login

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.MainActivity
import com.benyq.mvvm.DrawableBuilder
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CheckFingerprintDialog
import com.benyq.mvvm.ext.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_finger_login.*

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note 指纹登陆
 */
@AndroidEntryPoint
class FingerLoginActivity : LifecycleActivity<LoginViewModel>() {

    private val mCheckFingerprintDialog by lazy { CheckFingerprintDialog.newInstance() }

    private lateinit var mFingerprintManager: FingerprintVerifyManager

    private lateinit var mAlphaAnim: AlphaAnimation

    override fun getLayoutId() = R.layout.activity_finger_login

    override fun initVM(): LoginViewModel = getViewModel()

    override fun initView() {
        super.initView()

        //为ivAvatarFinger 设置背景
        ivAvatarFinger.background = DrawableBuilder(this)
            .circleRadius(dip2px(50))
            .fill(getColorRef(R.color.color_3fa9a9a9))
            .build()


        val phoneNumber = ChatLocalStorage.phoneNumber
        tvUserAccount.text = if (phoneNumber.length == 11) {
            phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7)
        }else {
            phoneNumber
        }

        // 指纹动画
        mAlphaAnim = AlphaAnimation(1f, 0.1f)
        mAlphaAnim.duration = 3000L
        mAlphaAnim.repeatCount = -1
        mAlphaAnim.repeatMode = Animation.REVERSE
        ivFingerprint.startAnimation(mAlphaAnim)

        initFingerprintManager()
        mFingerprintManager.authenticate()

        mCheckFingerprintDialog.show(supportFragmentManager)
    }

    override fun initListener() {
        super.initListener()
        llFingerPrintLogin.setOnClickListener {
            //指纹登陆
            mCheckFingerprintDialog.show(supportFragmentManager)
        }
        tvSwitchLoginPwd.setOnClickListener {
            goToActivity<LoginActivity>()
            finish()
        }
    }

    override fun dataObserver() {
        viewModelGet().mLoginResult.observe(this, Observer {
            goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
//        mAlphaAnim.cancel()
        ivFingerprint.clearAnimation()
        mFingerprintManager.closeAuthenticate()
    }

    private fun initFingerprintManager() {
        mFingerprintManager = FingerprintVerifyManager(this, {
            loge("识别成功")
            mCheckFingerprintDialog.dismiss()
            goToActivity<MainActivity>(exitAnim = R.anim.normal_out)
            finish()
        }, {
            mCheckFingerprintDialog.verifyMessage(it)
            loge("识别失败")
        })
    }
}