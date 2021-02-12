package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.bean.ContractSectionBean
import com.benyq.guochat.model.rep.ContractsRepository
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractsViewModel@ViewModelInject constructor(private val mRepository: ContractsRepository) : BaseViewModel(){

    val mContractsData = MutableLiveData<List<ContractSectionBean>>()

    fun getAllContracts() {
        quickLaunch<List<ContractSectionBean>> {
            onSuccess { mContractsData.value = it }
            request { mRepository.getAllContracts() }
        }
    }
}