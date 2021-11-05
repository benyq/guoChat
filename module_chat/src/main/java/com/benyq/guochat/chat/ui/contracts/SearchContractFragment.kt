package com.benyq.guochat.chat.ui.contracts

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.NavHostFragment
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.FragmentSearchContractBinding
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.module_base.ext.*
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @author benyq
 * @date 2021/10/28
 * @email 1520063035@qq.com
 * 搜索 联系人
 */
@AndroidEntryPoint
class SearchContractFragment : LifecycleFragment<ContractViewModel, FragmentSearchContractBinding>(){

    override fun provideViewBinding(): FragmentSearchContractBinding  = FragmentSearchContractBinding.inflate(layoutInflater)
    override fun initVM(): ContractViewModel = getViewModel()

    override fun initListener() {
        binding.tvCancel.setOnClickListener {
            NavHostFragment.findNavController(this).navigateUp()
        }
        binding.llPreSearch.setOnClickListener {
            mViewModel.searchContract(binding.etSearchKey.textTrim())
        }
        binding.etSearchKey.addTextChangedListener {
            binding.etSearchKey.textTrim().run {
                if (isNotEmpty()) {
                    binding.llPreSearch.visible()
                    binding.tvNonExistent.gone()
                }else {
                    binding.llPreSearch.gone()
                }
                binding.tvSearchKey.text = this
            }
        }
        binding.etSearchKey.run {
            requestFocus()
            val imm: InputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            //SHOW_FORCED表示强制显示
            imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
        }
    }

    override fun dataObserver() {
        mViewModel.searchContractData.observe(viewLifecycleOwner) { contract ->
            contract?.let {
                goToActivity<ApplyContractActivity>(IntentExtra.contractData to it)
            } ?: binding.tvNonExistent.visible()
        }
    }
}