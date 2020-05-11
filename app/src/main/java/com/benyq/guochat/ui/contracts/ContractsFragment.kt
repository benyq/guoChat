package com.benyq.guochat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.model.vm.ContractsViewModel
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.mvvm.annotation.BindViewModel
import kotlinx.android.synthetic.main.fragment_contracts.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractsFragment : LifecycleFragment(){

    private val mAdapter = ContractsSectionAdapter()
    @BindViewModel
    lateinit var mViewModel: ContractsViewModel

    override fun getLayoutId() = R.layout.fragment_contracts

    override fun initView() {
        rvContracts.layoutManager = LinearLayoutManager(mContext)
        rvContracts.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->

        }
    }

    override fun initListener() {
        iconAddContract.setOnClickListener {
            
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