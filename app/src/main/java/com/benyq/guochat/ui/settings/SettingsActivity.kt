package com.benyq.guochat.ui.settings

import com.benyq.guochat.R
import com.benyq.guochat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.guochat.ui.common.CheckFingerprintDialog
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loge
import kotlinx.android.synthetic.main.activity_settings.*

/**
 * @author benyq
 * @time 2020/5/22
 * @e-mail 1520063035@qq.com
 * @note 总的设置界面
 */
class SettingsActivity : BaseActivity() {

    private val mCheckFingerprintDialog by lazy { CheckFingerprintDialog.newInstance() }

    private lateinit var mFingerprintManager: FingerprintVerifyManager

    private val personConfig = ChatLocalStorage.personConfig


    override fun getLayoutId() = R.layout.activity_settings

    override fun initView() {
        super.initView()
        initFingerprintManager()
    }

    override fun initListener() {
        headerView.setBackAction { finish() }

        ifAboutApp.setOnClickListener {
            goToActivity<AboutAppActivity>()
        }

        sfFingerprintLogin.setChecked(personConfig.fingerprintLogin)

        sfFingerprintLogin.getSwitchButton().run {
            setOnClickListener {
                sfFingerprintLogin.getSwitchButton().isChecked = !isChecked
                mCheckFingerprintDialog.show(supportFragmentManager)
                mFingerprintManager.authenticate()
            }
        }

        tvLogout.setOnClickListener {
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
            sfFingerprintLogin.setChecked(personConfig.fingerprintLogin)
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
