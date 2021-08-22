package com.benyq.guochat.wanandroid.model.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.wanandroid.model.LoginData
import com.benyq.guochat.wanandroid.LocalCache
import com.benyq.guochat.wanandroid.model.repository.LoginRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @author benyq
 * @date 2021/8/5
 * @email 1520063035@qq.com
 */
@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : BaseViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    val loginResult = MutableLiveData<LoginData>()

    fun login() {
        quickLaunch<LoginData> {
            onSuccess {
                LocalCache.loginData = it!!
                loginResult.value = it
            }
            request {
                repository.login(username, password)
            }
        }
    }

}