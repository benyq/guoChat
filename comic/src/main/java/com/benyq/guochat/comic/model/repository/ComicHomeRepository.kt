package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.benyq.guochat.comic.model.http.ComicApiService
import com.benyq.guochat.comic.model.http.ComicResponse
import com.benyq.mvvm.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicHomeRepository @Inject constructor(private val apiService: ComicApiService): BaseRepository(){

    suspend fun boutiqueList(sexType: Int): ComicResponse<RecommendEntity>{
        return launchIO {
            apiService.boutiqueList(sexType)
        }
    }
}