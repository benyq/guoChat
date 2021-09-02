package com.benyq.guochat.wanandroid.model.repository

import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.WechatAuthorData
import com.benyq.guochat.wanandroid.model.http.PageData
import com.benyq.guochat.wanandroid.model.http.WanAndroidApi
import com.benyq.guochat.wanandroid.model.http.WanResult
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @date 2021/9/2
 * @email 1520063035@qq.com
 */

class WechatRepository @Inject constructor(private val apiService: WanAndroidApi) :
    BaseRepository() {

    suspend fun listWechatAuthor(): WanResult<List<WechatAuthorData>> {
        return apiService.listWechatAuthor()
    }


    suspend fun listWechatArticle(page: Int, id: String): WanResult<PageData<ArticleData>> {
        return try {
            apiService.listWechatArticle(id, page)
        } catch (t: Throwable) {
            WanResult.error(t.message ?: t.cause?.message ?: "未知错误")
        }
    }

}