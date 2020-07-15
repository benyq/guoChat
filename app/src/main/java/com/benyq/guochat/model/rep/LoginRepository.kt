package com.benyq.guochat.model.rep

import com.benyq.guochat.app.JSON
import com.benyq.guochat.mapOfToBodyJson
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.net.ServiceFactory
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.BaseRepository
import kotlinx.coroutines.delay
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginRepository : BaseRepository() {

    suspend fun login(username: String, pwd: String): ChatResponse<Boolean> {
        return launchIO {
            delay(300)
            if (username == "benyq" && pwd == "123456") {
                ChatResponse.success(true)
            }else {
                ChatResponse.error("账号或者密码错误")
            }
        }
    }

    suspend fun register(username: String, pwd: String): ChatResponse<String> {
        return launchIO {
            ServiceFactory.apiService.register(mapOfToBodyJson("userName" to username, "passWord" to pwd))
        }
    }

}