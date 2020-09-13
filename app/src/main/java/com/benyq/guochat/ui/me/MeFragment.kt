package com.benyq.guochat.ui.me

import com.benyq.guochat.R
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.loadImage
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.ui.base.BaseFragment
import com.benyq.guochat.ui.contracts.CallingCardActivity
import com.benyq.guochat.ui.settings.SettingsActivity
import com.benyq.mvvm.ext.goToActivity
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.coroutines.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MeFragment : BaseFragment() {

    private val mJob = Job()

    override fun getLayoutId() = R.layout.fragment_me

    override fun initView() {
    }

    override fun initListener() {
        llChatQr.setOnClickListener {
            goToActivity<CallingCardActivity>()
        }

        ifPersonalInfo.setOnClickListener {
            goToActivity<PersonalInfoActivity>()
        }

        ifSettings.setOnClickListener {
            goToActivity<SettingsActivity>()
        }

        ifNotificationTest.setOnClickListener {
            val launch = CoroutineScope(mJob + Dispatchers.Main)
            var progress = 0
            launch.launch {
                withContext(Dispatchers.IO) {
                    while (progress <= 100) {
                        delay(200)
                        NotificationHelper.createProgressNotification(requireContext(), progress)
                        progress++
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showUserInfo()
    }

    private fun showUserInfo() {
        LocalStorage.userAccount.run {
            ivAvatar.loadImage(avatarUrl)
            tvUserNick.text = nickName
            tvChatNo.text = chatNo
        }
    }
}
