package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.app.baseUrl
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.bean.ContractSectionBean
import com.benyq.guochat.chat.model.rep.ContractRepository
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.loge
import com.benyq.module_base.http.BenyqResponse
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
class ContractViewModel @Inject constructor(private val mRepository: ContractRepository) :
    BaseViewModel() {

    val mContractsData = MutableLiveData<List<ContractSectionBean>>()
    val searchContractData = MutableLiveData<ContractBean?>()
    val applyContractData = MutableLiveData<Boolean>()
    val applyContractRecord = MutableLiveData<List<ContractBean>>()
    val refreshContract = MutableLiveData<String>()

    fun getAllContracts() {
        quickLaunch<List<ContractSectionBean>> {
            onSuccess { mContractsData.value = it }
            request { mRepository.getAllContracts(ChatLocalStorage.uid) }
        }
    }


    fun searchContract(key: String) {
        quickLaunch<ContractBean> {
            onStart { showLoading("正在搜索联系人") }
            onSuccessRsp {
                val response : ChatResponse<ContractBean> = it as ChatResponse<ContractBean>
                when {
                    response.code == 8 -> {
                        searchContractData.value = null
                    }
                    response.isSuccess() -> {
                        response.getRealData()?.run {
                            avatar = baseUrl + avatar
                        }
                        loge("avatar ${response.getRealData()?.avatar}")
                        searchContractData.value = response.getRealData()
                    }
                    else -> {
                        Toasts.show(response.getMessage())
                    }
                }

            }
            onError {
                loge("searchContract error: ${it.message}")
            }
            onFinal { hideLoading() }
            request { mRepository.searchContract(key) }
        }
    }

    fun applyContract(uid: String) {
        quickLaunch<String> {
            onStart { showLoading("正在添加到通讯录") }
            onSuccess { applyContractData.value = true }
            onFinal { hideLoading() }
            onError { loge("applyContract error ${it.message}") }
            request { mRepository.applyContract(uid) }
        }
    }

    fun searchContractByCode(uid: String, code: String) {
        quickLaunch<ContractBean> {
            onStart { showLoading("") }
            onSuccess { searchContractData.value = it }
            onError { loadingType.value = UiState(isError = it) }
            onFinal { hideLoading() }
            request { mRepository.searchContractByCode(uid, code) }
        }
    }

    fun getApplyContractRecord() {
        quickLaunch<List<ContractBean>> {
            onStart { showLoading("") }
            onSuccess { applyContractRecord.value = it ?: listOf() }
            onError { loge("getApplyContractRecord error: ${it.message}") }
            onFinal { hideLoading() }
            request { mRepository.getApplyContractRecord() }
        }
    }

    fun agreeContract(id: String, applyId: String, uid: String){
        quickLaunch<String> {
            onSuccess { refreshContract.value = it }
            onError { loge("agreeContract error: ${it.message}") }
            request { mRepository.agreeContract(id, applyId, uid) }
        }
    }

    fun refuseContract(id: String){
        quickLaunch<String> {
            onSuccess { refreshContract.value = it }
            onError { loge("agreeContract error: ${it.message}") }
            request { mRepository.refuseContract(id) }
        }
    }
}

