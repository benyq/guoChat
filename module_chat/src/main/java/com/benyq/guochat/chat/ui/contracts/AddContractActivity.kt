package com.benyq.guochat.chat.ui.contracts

import com.benyq.guochat.chat.databinding.ActivityAddContractBinding
import com.benyq.module_base.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/8
 * @e-mail 1520063035@qq.com
 * @note 添加朋友
 */
@AndroidEntryPoint
class AddContractActivity : BaseActivity<ActivityAddContractBinding>() {

    override fun provideViewBinding() = ActivityAddContractBinding.inflate(layoutInflater)

}
