package com.benyq.guochat.chat.ui.me

import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.ActivityPersonalInfoBinding
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.guochat.chat.ui.contracts.CallingCardActivity
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loadImage

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 个人信息
 */
class PersonalInfoActivity : BaseActivity<ActivityPersonalInfoBinding>() {

    override fun provideViewBinding() = ActivityPersonalInfoBinding.inflate(layoutInflater)

    override fun initView() {
        ChatLocalStorage.userAccount.run {
            binding.ivAvatar.loadImage(avatarUrl)
            binding.ifNickName.setContent(nickName)
            binding.ifChatNo.setContent(chatNo)
        }
    }

    override fun initListener() {

        binding.headerView.setBackAction { finish() }

        binding.llAvatar.setOnClickListener {
            goToActivity<AvatarActivity>()
        }
        binding.ifNickName.setOnClickListener {
            SmartJump.from(this).startActivity(PersonalInfoEditActivity::class.java, R.anim.slide_right_in, R.anim.slide_left_out)
        }

        binding.ifCallCardQR.setOnClickListener {
            goToActivity<CallingCardActivity>()
        }

    }
}
