package com.benyq.guochat

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.rep.MainRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @author benyq
 * @date 2021/11/8
 * @email 1520063035@qq.com
 *
 */
@HiltViewModel
class SplashViewModel @Inject constructor(private val mRepository: MainRepository) : BaseViewModel(){

    val checkResult = MutableLiveData<Boolean>()

    fun checkToken() {
        quickLaunch<Boolean> {
            onStart { showLoading("") }
            onSuccess { checkResult.value = it }
            onError {  }
            onFinal { hideLoading() }
            request {
                val response = mRepository.checkToken()
                if (response.isSuccess()) {
                    mRepository.refreshUserData(ChatLocalStorage.uid)
                }else {
                    ChatResponse.success(false)
                }
            }
        }
    }

}