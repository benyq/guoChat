package com.benyq.guochat.wanandroid.model.repository

import com.benyq.guochat.wanandroid.model.PersonScoreData
import com.benyq.guochat.wanandroid.model.WanResult
import com.benyq.guochat.wanandroid.model.http.WanAndroidApi
import javax.inject.Inject

/**
 * @author benyq
 * @time 2021/8/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MeRepository @Inject constructor(private val apiService: WanAndroidApi){

    suspend fun getCoinCount() : WanResult<PersonScoreData> {
        return apiService.personScore()
    }

}