package com.benyq.guochat.chat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.FragmentContractsBinding
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleFragment
import com.benyq.module_base.ext.goToActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ContractsFragment : LifecycleFragment<ContractViewModel, FragmentContractsBinding>() {

    private val mAdapter = ContractsSectionAdapter()

    override fun initVM(): ContractViewModel = getViewModel()

    override fun provideViewBinding() = FragmentContractsBinding.inflate(layoutInflater)

    override fun initView() {
        binding.rvContracts.layoutManager = LinearLayoutManager(mContext)
        binding.rvContracts.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val contractSectionBean = mAdapter.data[position]
            if (!contractSectionBean.isHeader) {
                with(contractSectionBean.contractEntity!!) {
                    val contractBean = ContractBean(0, ownUserId, contractId, chatNo, nick, remark, gender, avatarUrl)
                    goToActivity<ContractDetailActivity>(IntentExtra.contractData to contractBean)
                }

            }
        }
    }

    override fun initListener() {
        binding.iconAddContract.setOnClickListener {
            goToActivity<NewContractActivity>()
        }
        binding.iconContractCard.setOnClickListener {
//            goToActivity<ContractIdCardActivity>()
        }
    }

    override fun dataObserver() {
        mViewModel.mContractsData.observe(viewLifecycleOwner){
            mAdapter.setNewInstance(it.toMutableList())
        }
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getAllContracts()
    }
}