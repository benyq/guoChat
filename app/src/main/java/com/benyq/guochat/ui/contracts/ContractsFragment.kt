package com.benyq.guochat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.model.vm.ContractsViewModel
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.startActivity
import kotlinx.android.synthetic.main.fragment_contracts.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractsFragment : LifecycleFragment<ContractsViewModel>() {

    private val mAdapter = ContractsSectionAdapter()

    override fun initVM(): ContractsViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_contracts

    override fun initView() {
        rvContracts.layoutManager = LinearLayoutManager(mContext)
        rvContracts.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val contractBean = mAdapter.data[position]
            if (!contractBean.isHeader && contractBean.contractEntity != null) {
                startActivity<ContractDetailActivity>(IntentExtra.contractData to contractBean.contractEntity)
            }
        }
    }

    override fun initListener() {
        iconAddContract.setOnClickListener {
            startActivity<AddContractActivity>()
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