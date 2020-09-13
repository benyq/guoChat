package com.benyq.guochat.ui.me

import com.benyq.guochat.R
import com.benyq.guochat.loadImage
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.guochat.ui.contracts.CallingCardActivity
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.goToActivity
import kotlinx.android.synthetic.main.activity_personal_info.*

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 个人信息
 */
class PersonalInfoActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_personal_info

    override fun initView() {
        LocalStorage.userAccount.run {
            ivAvatar.loadImage(avatarUrl)
            ifNickName.setContent(nickName)
            ifChatNo.setContent(chatNo)
        }
    }

    override fun initListener() {

        headerView.setBackAction { finish() }

        llAvatar.setOnClickListener {
            goToActivity<AvatarActivity>()
        }
        ifNickName.setOnClickListener {
            SmartJump.from(this).startActivity(PersonalInfoEditActivity::class.java, R.anim.slide_right_in, R.anim.slide_right_out)
        }

        ifCallCardQR.setOnClickListener {
            goToActivity<CallingCardActivity>()
        }

    }
}
