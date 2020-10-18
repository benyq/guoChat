package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.local.BookShelfTable_
import com.benyq.guochat.comic.local.ComicObjectBox
import com.benyq.guochat.comic.local.SearchHistoryRecord_
import com.benyq.guochat.comic.model.http.ComicResponse
import com.benyq.mvvm.mvvm.BaseRepository
import io.objectbox.kotlin.query
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicShelfRepository @Inject constructor(): BaseRepository() {

    suspend fun getShelfBook(): ComicResponse<List<BookShelfTable>> {
        return launchIO {
            ComicObjectBox.bookShelfBox.run {
                val bookList = query().orderDesc(BookShelfTable_.readTime).build().find()
                ComicResponse.success(bookList)
            }
        }
    }
}