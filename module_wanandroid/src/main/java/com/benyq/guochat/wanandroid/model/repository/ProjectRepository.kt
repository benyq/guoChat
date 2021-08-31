package com.benyq.guochat.wanandroid.model.repository

import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.ProjectTreeData
import com.benyq.guochat.wanandroid.model.http.PageData
import com.benyq.guochat.wanandroid.model.http.WanAndroidApi
import com.benyq.guochat.wanandroid.model.http.WanResult
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 *
 * @author benyq
 * @date 2021/8/30
 * @email 1520063035@qq.com
 *
 */
class ProjectRepository @Inject constructor(private val apiService: WanAndroidApi) :
    BaseRepository() {

    suspend fun listProjectsTab(): WanResult<List<ProjectTreeData>> {
        return apiService.listProjectsTab()
    }

    suspend fun listProjects(page: Int, cid: String): WanResult<PageData<ArticleData>> {
        return try {
            apiService.listProjects(page, cid)
        }catch (t: Throwable) {
            WanResult.error(t.message ?: t.cause?.message ?: "未知错误")
        }
    }

}