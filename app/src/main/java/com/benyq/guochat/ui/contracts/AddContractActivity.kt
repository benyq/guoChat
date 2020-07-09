package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.mvvm.ext.startActivity
import kotlinx.android.synthetic.main.activity_add_contract.*

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 添加朋友
 */
class AddContractActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_add_contract

    override fun initListener() {
        llQuery.setOnClickListener {
            //这个可能用到Navigation 当前界面 -> 搜索界面 -> 其他用户界面
        }
        llChatIdCode.setOnClickListener {
            //跳转到二维码名片界面
            startActivity<CallingCardActivity>()
        }
        ilScanCode.setOnClickListener {
            startActivity<CaptureActivity>()
        }

        headerView.setBackAction {
            finish()
        }
    }

    override fun initView() {
        //从本地缓存中获取User信息
        val userChatId = "yzjbenyq"
        val contentChatId = "${getString(R.string.my_chat_id)} $userChatId"
        tvGuoChatId.text = contentChatId
    }
}
