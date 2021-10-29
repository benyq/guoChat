package com.benyq.guochat.chat.ui.contracts

import android.annotation.SuppressLint
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityApplyContractBinding
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.loadImage
import com.benyq.module_base.ui.base.LifecycleActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @date 2021/10/29
 * @email 1520063035@qq.com
 * 联系人申请
 */
@AndroidEntryPoint
class ApplyContractActivity : LifecycleActivity<ContractViewModel, ActivityApplyContractBinding>() {
    override fun provideViewBinding(): ActivityApplyContractBinding =
        ActivityApplyContractBinding.inflate(layoutInflater)

    override fun initVM(): ContractViewModel = getViewModel()


    private lateinit var mContractEntity: ContractBean

    @SuppressLint("SetTextI18n")
    override fun initView() {
        //根据传过来的联系人信息初始化页面
        mContractEntity = intent.getParcelableExtra(IntentExtra.contractData)!!
        binding.ivAvatar.loadImage(mContractEntity.avatar)
        binding.tvNickName.text = mContractEntity.nick
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }

        binding.ifGuoChatCircle.setOnClickListener {

        }
        binding.llApplyContract.setOnClickListener {
            viewModelGet().applyContract(mContractEntity.contractId)
        }
    }


    override fun dataObserver() {
        with(viewModelGet()) {
            applyContractData.observe(this@ApplyContractActivity) {
                Toasts.show("联系人申请已发送")
                finish()
            }
            loadingType.observe(this@ApplyContractActivity) {
                if (it.isLoading) {
                    showLoading(it.isSuccess)
                } else {
                    hideLoading()
                }
            }
        }
    }

}