package com.benyq.guochat.openeye.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.openeye.model.repository.OpenEyeRepository
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class OpenEyeSearchViewModel @ViewModelInject constructor(private val mRepository: OpenEyeRepository) : BaseViewModel(){

    val mHotKeywordsData = MutableLiveData<List<String>>()

    fun getHotSearch(){
        quickLaunch<List<String>> {
            onSuccess {
                mHotKeywordsData.value = it
            }
            onError {

            }
            request { mRepository.getHotSearch() }
        }
    }
}

