package com.benyq.guochat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.rep.LoginRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginViewModel(private val mRepository: LoginRepository) : BaseViewModel() {

    val mLoginResult = MutableLiveData<Boolean>()
    val mRegisterResult = MutableLiveData<String>()

    fun login(username: String, pwd: String) {
        quickLaunch<Boolean> {
            onStart { showLoading("正在登录") }
            onSuccess { mLoginResult.value = it }
            onFinal { hideLoading() }
            request { mRepository.login(username, pwd) }
        }
    }

    fun register(username: String, pwd: String){
        quickLaunch<String> {
            onStart { showLoading("") }
            onSuccess { mRegisterResult.value = it }
            onFinal { hideLoading() }
            request { mRepository.register(username, pwd) }
        }
    }
}