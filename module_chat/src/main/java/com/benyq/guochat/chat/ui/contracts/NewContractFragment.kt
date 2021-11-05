package com.benyq.guochat.chat.ui.contracts

import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.FragmentNewContractBinding
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @author benyq
 * @date 2021/11/4
 * @email 1520063035@qq.com
 *
 */
@AndroidEntryPoint
class NewContractFragment : LifecycleFragment<ContractViewModel, FragmentNewContractBinding>(){
    override fun provideViewBinding(): FragmentNewContractBinding = FragmentNewContractBinding.inflate(layoutInflater)
    override fun initVM(): ContractViewModel = getViewModel()

    private val mAdapter by lazy { ApplyContractRecordAdapter() }

    override fun initListener() {
        binding.headerView.setBackAction { requireActivity().finish() }
        binding.tvAddContract.setOnClickListener {
            goToActivity<AddContractActivity>()
        }

        binding.rvApplyRecord.layoutManager = LinearLayoutManager(requireContext())
        binding.rvApplyRecord.adapter = mAdapter
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            val id = mAdapter.data[position].id.toString()
            val applyId = mAdapter.data[position].contractId
            if (view.id == R.id.btnRefuse) {
                mViewModel.refuseContract(id)
            }else if (view.id == R.id.btnAgree) {
                mViewModel.agreeContract(id, applyId, ChatLocalStorage.uid)
            }
        }
        binding.llQuery.setOnClickListener {
            NavHostFragment.findNavController(this).navigate(R.id.action_search_contract)
        }
    }

    override fun initData() {
        mViewModel.getApplyContractRecord()
    }

    override fun dataObserver() {
        with(mViewModel) {
//            loadingType.observe(viewLifecycleOwner) {
//                if (it.isLoading) {
//                    showLoading("")
//                }else {
//                    hideLoading()
//                }
//            }
            applyContractRecord.observe(viewLifecycleOwner) {
                mAdapter.setNewInstance(it.toMutableList())
            }

            refreshContract.observe(viewLifecycleOwner) {
                initData()
            }
        }
    }
}