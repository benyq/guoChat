package com.benyq.guochat.wanandroid.model.repository

import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.BannerData
import com.benyq.guochat.wanandroid.model.http.PageData
import com.benyq.guochat.wanandroid.model.http.WanResult
import com.benyq.guochat.wanandroid.model.http.WanAndroidApi
import com.benyq.module_base.mvvm.BaseRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

/**
 * @author benyq
 * @time 2021/8/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MainRepository @Inject constructor(private val apiService: WanAndroidApi) : BaseRepository() {

    suspend fun articleList(page: Int): WanResult<PageData<ArticleData>> {
        return apiService.articleList(page)
    }

    suspend fun banner(): WanResult<List<BannerData>> {
        return apiService.banner()
    }
}