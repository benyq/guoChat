package com.benyq.guochat.ui.contracts

import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.databinding.ActivityContractNickBinding
import com.benyq.guochat.local.entity.ContractEntity
import com.benyq.module_base.ui.base.BaseActivity
import com.benyq.module_base.ext.textTrim

/**
 * @author benyq
 * @time 2020/5/31
 * @e-mail 1520063035@qq.com
 * @note  修改联系人昵称
 */
class ContractNickActivity : BaseActivity<ActivityContractNickBinding>() {

    private lateinit var mContractData: ContractEntity

    override fun provideViewBinding() = ActivityContractNickBinding.inflate(layoutInflater)

    override fun initView() {
        mContractData = intent.getParcelableExtra(IntentExtra.contractData)!!

        binding.headerView.setBackAction { finish() }
        binding.headerView.setMenuBtnAction {
            //保存数据
            val remarks = binding.etRemarksName.textTrim()

        }

        binding.etRemarksName.setText(mContractData.nick)
        binding.etRemarksName.setSelection(mContractData.nick.length)
    }
}