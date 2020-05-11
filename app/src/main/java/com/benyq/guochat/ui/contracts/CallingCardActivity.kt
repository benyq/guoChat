package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.ui.base.LifecycleActivity
import kotlinx.android.synthetic.main.activity_calling_card.*

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 二维码名片
 *  预想的是根据传入的果聊号生成名片信息
 */
class CallingCardActivity : LifecycleActivity() {

    override fun getLayoutId() = R.layout.activity_calling_card

    override fun initListener() {
        headerView.setBackAction { finish() }
    }
}
