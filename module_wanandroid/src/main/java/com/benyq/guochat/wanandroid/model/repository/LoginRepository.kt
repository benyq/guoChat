package com.benyq.guochat.wanandroid.model.repository

import com.benyq.guochat.wanandroid.model.LoginData
import com.benyq.guochat.wanandroid.model.WanResult
import com.benyq.guochat.wanandroid.model.http.WanAndroidApi
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2021/8/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class LoginRepository @Inject constructor(private val apiService: WanAndroidApi): BaseRepository() {

    suspend fun login(username: String, password: String): WanResult<LoginData> {
        return apiService.login(username, password)
    }

}