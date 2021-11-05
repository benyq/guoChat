package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.mapOfToBodyJson
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.bean.UserBean
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.module_base.mvvm.BaseRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginRepository @Inject constructor(private val apiService: ChatApiService) : BaseRepository() {

    suspend fun login(username: String, pwd: String): ChatResponse<UserBean> {
        return apiService.login(username, pwd)
    }

    suspend fun register(username: String, pwd: String): ChatResponse<UserBean> {
        return launchIO {
            apiService.register(mapOfToBodyJson("phone" to username, "pwd" to pwd))
        }
    }

}