package com.benyq.guochat.ui.me

import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.loadAvatar
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.guochat.ui.contracts.CallingCardActivity
import com.benyq.guochat.ui.settings.SettingsActivity
import com.benyq.mvvm.ext.startActivity
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MeFragment : LifecycleFragment() {

    override fun getLayoutId() = R.layout.fragment_me

    override fun initView() {
    }

    override fun initListener() {
        llChatQr.setOnClickListener {
            startActivity<CallingCardActivity>()
        }

        ifPersonalInfo.setOnClickListener {
            startActivity<PersonalInfoActivity>()
        }

        ifSettings.setOnClickListener {
            startActivity<SettingsActivity>()
        }
    }

    override fun onResume() {
        super.onResume()
        showUserInfo()
    }

    private fun showUserInfo(){
        LocalStorage.userAccount.run {
            loadAvatar(ivAvatar, avatarUrl)
            tvUserNick.text = nickName
            tvChatNo.text = chatNo
        }
    }
}
