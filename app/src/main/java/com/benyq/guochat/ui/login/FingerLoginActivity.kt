package com.benyq.guochat.ui.login

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.databinding.ActivityFingerLoginBinding
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.model.vm.LoginViewModel
import com.benyq.guochat.ui.MainActivity
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.CheckFingerprintDialog
import com.benyq.module_base.ext.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note 指纹登陆
 */
@AndroidEntryPoint
class FingerLoginActivity : LifecycleActivity<LoginViewModel, ActivityFingerLoginBinding>() {

    private val mCheckFingerprintDialog by lazy { CheckFingerprintDialog.newInstance() }

    private lateinit var mFingerprintManager: FingerprintVerifyManager

    private lateinit var mAlphaAnim: AlphaAnimation

    override fun provideViewBinding() = ActivityFingerLoginBinding.inflate(layoutInflater)

    override fun initVM(): LoginViewModel = getViewModel()

    override fun initView() {
        super.initView()

        //为ivAvatarFinger 设置背景
        binding.ivAvatarFinger.background = DrawableBuilder(this)
            .circleRadius(dip2px(50))
            .fill(getColorRef(R.color.color_3fa9a9a9))
            .build()


        val phoneNumber = ChatLocalStorage.phoneNumber
        binding.tvUserAccount.text = if (phoneNumber.length == 11) {
            phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7)
        }else {
            phoneNumber
        }

        // 指纹动画
        mAlphaAnim = AlphaAnimation(1f, 0.1f)
        mAlphaAnim.duration = 3000L
        mAlphaAnim.repeatCount = -1
        mAlphaAnim.repeatMode = Animation.REVERSE
        binding.ivFingerprint.startAnimation(mAlphaAnim)

        initFingerprintManager()
        mFingerprintManager.authenticate()

        mCheckFingerprintDialog.show(supportFragmentManager)
    }

    override fun initListener() {
        super.initListener()
        binding.llFingerPrintLogin.setOnClickListener {
            //指纹登陆
            mCheckFingerprintDialog.show(supportFragmentManager)
        }
        binding.tvSwitchLoginPwd.setOnClickListener {
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
        binding.ivFingerprint.clearAnimation()
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