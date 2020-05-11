package com.benyq.guochat.model.rep

import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.mvvm.mvvm.BaseRepository
import kotlinx.coroutines.delay

/**
 * @author benyq
 * @time 2020/5/7
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginRepository : BaseRepository() {

    suspend fun login(username: String, pwd: String): ChatResponse<Boolean> {
        return launchIO {
            if (username == "benyq" && pwd == "123456") {
                ChatResponse.success(true)
            }else {
                ChatResponse.error("账号或者密码错误")
            }
        }
    }
}