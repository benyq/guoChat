package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.model.bean.ComicDetailResponse
import com.benyq.guochat.comic.model.http.ComicApiService
import com.benyq.guochat.comic.model.http.ComicResponse
import com.benyq.mvvm.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/10/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class BookDetailRepository @Inject constructor(private val apiService: ComicApiService): BaseRepository(){
    suspend fun getComicDetail(comicId: String): ComicResponse<ComicDetailResponse> {
        return launchIO {
            apiService.comicDetail(comicId)
        }
    }
}