package com.benyq.guochat.ui.contracts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.local.entity.ContractEntity
import com.benyq.guochat.textTrim
import com.benyq.guochat.ui.base.LifecycleActivity
import kotlinx.android.synthetic.main.activity_contract_nick.*

/**
 * @author benyq
 * @time 2020/5/31
 * @e-mail 1520063035@qq.com
 * @note  修改联系人昵称
 */
class ContractNickActivity : LifecycleActivity() {

    private lateinit var mContractData: ContractEntity

    override fun getLayoutId() = R.layout.activity_contract_nick

    override fun initView() {
        mContractData = intent.getParcelableExtra(IntentExtra.contractData)!!

        headerView.setBackAction { finish() }
        headerView.setMenuBtnAction {
            //保存数据
            val remarks = etRemarksName.textTrim()

        }

        etRemarksName.setText(mContractData.nick)
        etRemarksName.setSelection(mContractData.nick.length)
    }
}