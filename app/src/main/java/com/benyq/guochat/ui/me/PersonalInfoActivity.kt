package com.benyq.guochat.ui.me

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.R
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.contracts.CallingCardActivity
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.startActivity
import kotlinx.android.synthetic.main.activity_personal_info.*

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 个人信息
 */
class PersonalInfoActivity : LifecycleActivity() {

    override fun getLayoutId() = R.layout.activity_personal_info

    override fun initListener() {

        headerView.setBackAction { finish() }

        llAvatar.setOnClickListener {
            startActivity<AvatarActivity>()
        }
        ifNickName.setOnClickListener {
            SmartJump.from(this).startActivity(PersonalInfoEditActivity::class.java)
        }

        ifCallCardQR.setOnClickListener {
            startActivity<CallingCardActivity>()
        }

    }
}
