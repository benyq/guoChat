package com.benyq.guochat.chat.ui.settings

import android.content.Intent
import com.benyq.guochat.chat.databinding.ActivitySettingsBinding
import com.benyq.guochat.chat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.ui.common.CheckFingerprintDialog
import com.benyq.guochat.chat.ui.common.CommonBottomDialog
import com.benyq.guochat.chat.ui.login.LoginActivity
import com.benyq.module_base.ActivityManager
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.BaseActivity
import kotlin.system.exitProcess

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note 总的设置界面
 */
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    override fun provideViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)

    private val mCheckFingerprintDialog by lazy { CheckFingerprintDialog.newInstance() }

    private lateinit var mFingerprintManager: FingerprintVerifyManager

    private val personConfig = ChatLocalStorage.personConfig

    private var mBottomDialog: CommonBottomDialog? = null

    override fun initView() {
        super.initView()
        initFingerprintManager()
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }

        binding.ifAboutApp.setOnClickListener {
            goToActivity<AboutAppActivity>()
        }

        if (FingerprintVerifyManager.canAuthenticate(this)) {
            binding.sfFingerprintLogin.setChecked(personConfig.fingerprintLogin)
        } else {
            binding.sfFingerprintLogin.gone()
        }

        binding.sfFingerprintLogin.getSwitchButton().run {
            setOnClickListener {
                binding.sfFingerprintLogin.getSwitchButton().isChecked = !isChecked
                mCheckFingerprintDialog.show(supportFragmentManager)
                mFingerprintManager.authenticate()
            }
        }

        binding.tvLogout.setOnClickListener {
            showBottomDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        ChatLocalStorage.personConfig = personConfig
    }


    private fun initFingerprintManager() {
        mFingerprintManager = FingerprintVerifyManager(this, {
            loge("识别成功")
            mCheckFingerprintDialog.dismiss()
            personConfig.fingerprintLogin = !personConfig.fingerprintLogin
            binding.sfFingerprintLogin.setChecked(personConfig.fingerprintLogin)
            mFingerprintManager.closeAuthenticate()
        }, {
            mCheckFingerprintDialog.verifyMessage(it)
            if (it.isNotEmpty()) {
                mFingerprintManager.closeAuthenticate()
            }
            loge("识别失败")
        })
    }


    private fun showBottomDialog() {
        mBottomDialog =
            mBottomDialog ?: CommonBottomDialog.newInstance(arrayOf("退出登录", "退出程序"))
                .apply {
                    setOnMenuAction { _, index ->
                        when (index) {
                            0 -> {
                                //删除数据
                                ChatLocalStorage.logout()
                                //跳转到LoginActivity
                                startActivity(
                                    Intent(
                                        this@SettingsActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                ActivityManager.finishAll()
                            }
                            1 -> {
                                ActivityManager.finishAll()
                                exitProcess(0)
                            }
                        }
                    }
                }
        mBottomDialog?.show(supportFragmentManager)
    }
}
