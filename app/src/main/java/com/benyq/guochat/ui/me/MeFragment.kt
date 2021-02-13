package com.benyq.guochat.ui.me

import com.benyq.guochat.databinding.FragmentMeBinding
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.module_base.ui.base.BaseFragment
import com.benyq.guochat.ui.contracts.CallingCardActivity
import com.benyq.guochat.ui.settings.SettingsActivity
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loadImage
import kotlinx.coroutines.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MeFragment : BaseFragment<FragmentMeBinding>() {

    private val mJob = Job()

    override fun provideViewBinding() = FragmentMeBinding.inflate(layoutInflater)

    override fun initView() {
    }

    override fun initListener() {
        binding.llChatQr.setOnClickListener {
            goToActivity<CallingCardActivity>()
        }

        binding.ifPersonalInfo.setOnClickListener {
            goToActivity<PersonalInfoActivity>()
        }

        binding.ifSettings.setOnClickListener {
            goToActivity<SettingsActivity>()
        }

        binding.ifNotificationTest.setOnClickListener {
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
        ChatLocalStorage.userAccount.run {
            binding.ivAvatar.loadImage(avatarUrl)
            binding.tvUserNick.text = nickName
            binding.tvChatNo.text = chatNo
        }
    }
}
