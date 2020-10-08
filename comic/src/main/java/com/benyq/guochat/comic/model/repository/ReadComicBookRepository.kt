package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.model.bean.ComicPreViewResponse
import com.benyq.guochat.comic.model.http.ComicApiService
import com.benyq.guochat.comic.model.http.ComicResponse
import com.benyq.mvvm.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note
 */
class ReadComicBookRepository @Inject constructor(private val apiService: ComicApiService): BaseRepository(){

    suspend fun comicPreView(chapterId: String): ComicResponse<ComicPreViewResponse> {
        return launchIO {
            apiService.comicPreView(chapterId)
        }
    }

}