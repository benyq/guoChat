package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.databinding.ActivityAddContractBinding
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.BaseActivity

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 添加朋友
 */
class AddContractActivity : BaseActivity<ActivityAddContractBinding>() {

    override fun provideViewBinding() = ActivityAddContractBinding.inflate(layoutInflater)

    override fun initListener() {
        binding.llQuery.setOnClickListener {
            //这个可能用到Navigation 当前界面 -> 搜索界面 -> 其他用户界面
        }
        binding.llChatIdCode.setOnClickListener {
            //跳转到二维码名片界面
            goToActivity<CallingCardActivity>()
        }
        binding.ilScanCode.setOnClickListener {
            goToActivity<CaptureActivity>()
        }

        binding.headerView.setBackAction {
            finish()
        }
    }

    override fun initView() {
        //从本地缓存中获取User信息
        val userChatId = "yzjbenyq"
        val contentChatId = "${getString(R.string.my_chat_id)} $userChatId"
        binding.tvGuoChatId.text = contentChatId
    }
}
