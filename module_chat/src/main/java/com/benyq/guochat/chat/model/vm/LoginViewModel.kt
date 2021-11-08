package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.app.baseUrl
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.PersonConfig
import com.benyq.guochat.chat.model.bean.UserBean
import com.benyq.guochat.chat.model.rep.LoginRepository
import com.benyq.guochat.chat.model.rep.MainRepository
import com.benyq.module_base.ext.md5
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val mainRepository: MainRepository
) :
    BaseViewModel() {

    val mLoginResult = MutableLiveData<Boolean>()
    val mRegisterResult = MutableLiveData<Boolean>()

    fun login(username: String, pwd: String) {
        quickLaunch<UserBean> {
            onStart { showLoading("正在登录") }
            onSuccess {
                //模拟登录之后获取配置
                val oldConfig = ChatLocalStorage.personConfig
                if (oldConfig.phoneNumber != username) {
                    ChatLocalStorage.personConfig = PersonConfig(username, false)
                }
                registerOrLogin(it!!)
                mLoginResult.value = true
            }
            onFinal { hideLoading() }
            request {
                val response = loginRepository.login(username, pwd.md5())
                if (response.isSuccess() && response.data != null) {
                    mainRepository.refreshUserData(response.data.chatId)
                }
                response
            }
        }
    }

    fun register(username: String, pwd: String) {
        quickLaunch<UserBean> {
            onStart { showLoading("") }
            onSuccess {
                registerOrLogin(it!!)
                mRegisterResult.value = true
            }
            onFinal { hideLoading() }
            request {
                val response = loginRepository.register(username, pwd.md5())
                if (response.isSuccess() && response.data != null) {
                    mainRepository.refreshUserData(response.data.chatId)
                }
                response
            }
        }
    }

    private fun registerOrLogin(user: UserBean) {
        ChatLocalStorage.phoneNumber = user.phone
        ChatLocalStorage.token = user.token
        ChatLocalStorage.uid = user.chatId
        user.avatarUrl = baseUrl + user.avatarUrl
        ChatLocalStorage.userAccount = user
    }
}