package com.benyq.guochat.chat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.chat.databinding.ActivityCOntractIdCardBinding
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/8/19
 * @e-mail 1520063035@qq.com
 * @note  这是为了尝试SnapHelper写的类
 */
@AndroidEntryPoint
class ContractIdCardActivity : LifecycleActivity<ContractViewModel, ActivityCOntractIdCardBinding>() {

    private val mAdapter = ContractIdCardAdapter()

    override fun provideViewBinding() = ActivityCOntractIdCardBinding.inflate(layoutInflater)

    override fun initVM(): ContractViewModel = getViewModel()

    override fun initView() {
        super.initView()
        binding.rvContractIdCard.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.rvContractIdCard.adapter = mAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvContractIdCard)
    }

    override fun initData() {
        super.initData()
        viewModelGet().getAllContracts()
    }

    override fun dataObserver() {
        viewModelGet().mContractsData.observe(this, Observer {
            val data = it.filter { bean ->
                bean.contractEntity != null
            }
            mAdapter.setNewInstance(data.toMutableList())
        })
    }

    override fun isSupportSwipeBack() = true

    override fun isOnlyTrackingLeftEdge() = false

}