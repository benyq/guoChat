package com.benyq.guochat.ui.contracts

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.R
import com.benyq.guochat.getViewModel
import com.benyq.guochat.model.vm.ContractsViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_c_ontract_id_card.*

/**
 * @author benyq
 * @time 2020/8/19
 * @e-mail 1520063035@qq.com
 * @note  这是为了尝试SnapHelper写的类
 */
@AndroidEntryPoint
class ContractIdCardActivity : LifecycleActivity<ContractsViewModel>() {

    private val mAdapter = ContractIdCardAdapter()

    override fun getLayoutId() = R.layout.activity_c_ontract_id_card

    override fun initVM(): ContractsViewModel = getViewModel()

    override fun initView() {
        super.initView()
        rvContractIdCard.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rvContractIdCard.adapter = mAdapter
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvContractIdCard)
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


}