package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.mapOfToBodyJson
import com.benyq.guochat.chat.model.bean.ChatResponse
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

    suspend fun login(username: String, pwd: String): ChatResponse<Boolean> {
        return launchIO {
            delay(300)
            if (username == "13512345678" && pwd == "123456") {
                ChatResponse.success<Boolean>(true)
            }else {
                ChatResponse.error("账号或者密码错误", -101)
            }
        }
    }

    suspend fun register(username: String, pwd: String): ChatResponse<String> {
        return launchIO {
            apiService.register(mapOfToBodyJson("userName" to username, "passWord" to pwd))
        }
    }

}