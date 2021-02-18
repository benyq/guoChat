package com.benyq.guochat.chat.ui.settings

import com.benyq.guochat.chat.databinding.ActivitySettingsBinding
import com.benyq.guochat.chat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.guochat.chat.ui.common.CheckFingerprintDialog
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loge

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note 总的设置界面
 */
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    private val mCheckFingerprintDialog by lazy { CheckFingerprintDialog.newInstance() }

    private lateinit var mFingerprintManager: FingerprintVerifyManager

    private val personConfig = ChatLocalStorage.personConfig

    override fun provideViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()
        initFingerprintManager()
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }

        binding.ifAboutApp.setOnClickListener {
            goToActivity<AboutAppActivity>()
        }

        binding.sfFingerprintLogin.setChecked(personConfig.fingerprintLogin)

        binding.sfFingerprintLogin.getSwitchButton().run {
            setOnClickListener {
                binding.sfFingerprintLogin.getSwitchButton().isChecked = !isChecked
                mCheckFingerprintDialog.show(supportFragmentManager)
                mFingerprintManager.authenticate()
            }
        }

        binding.tvLogout.setOnClickListener {
            //删除数据

            //跳转到LoginActivity 或者 FingerLoginActivity

        }
    }

    override fun onPause() {
        super.onPause()
        ChatLocalStorage.personConfig = personConfig
    }

    override fun onDestroy() {
        super.onDestroy()

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
}
