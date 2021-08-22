package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.model.bean.ContractSectionBean
import com.benyq.guochat.chat.model.rep.ContractsRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class ContractsViewModel @Inject constructor(private val mRepository: ContractsRepository) :
    BaseViewModel() {

    val mContractsData = MutableLiveData<List<ContractSectionBean>>()

    fun getAllContracts() {
        quickLaunch<List<ContractSectionBean>> {
            onSuccess { mContractsData.value = it }
            request { mRepository.getAllContracts() }
        }
    }
}