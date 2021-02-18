package com.benyq.guochat.chat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.FragmentContractsBinding
import com.benyq.guochat.chat.model.vm.ContractsViewModel
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
class ContractsFragment : LifecycleFragment<ContractsViewModel, FragmentContractsBinding>() {

    private val mAdapter = ContractsSectionAdapter()

    override fun initVM(): ContractsViewModel = getViewModel()

    override fun provideViewBinding() = FragmentContractsBinding.inflate(layoutInflater)

    override fun initView() {
        binding.rvContracts.layoutManager = LinearLayoutManager(mContext)
        binding.rvContracts.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val contractBean = mAdapter.data[position]
            if (!contractBean.isHeader && contractBean.contractEntity != null) {
                goToActivity<ContractDetailActivity>(IntentExtra.contractData to contractBean.contractEntity)
            }
        }
    }

    override fun initListener() {
        binding.iconAddContract.setOnClickListener {
            goToActivity<AddContractActivity>()
        }
        binding.iconContractCard.setOnClickListener {
            goToActivity<ContractIdCardActivity>()
        }
    }

    override fun initData() {
        mViewModel.getAllContracts()
    }

    override fun dataObserver() {
        mViewModel.mContractsData.observe(viewLifecycleOwner, Observer {
            mAdapter.setNewInstance(it.toMutableList())
        })
    }
}