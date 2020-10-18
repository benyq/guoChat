package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.local.BookShelfTable_
import com.benyq.guochat.comic.local.ComicObjectBox
import com.benyq.guochat.comic.model.bean.ComicDetailResponse
import com.benyq.guochat.comic.model.bean.ComicPreViewResponse
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

    suspend fun searchBookShelf(comicId: String): ComicResponse<BookShelfTable> {
        return launchIO {
            ComicObjectBox.bookShelfBox.run {
                val shelfBook: BookShelfTable? = query().equal(BookShelfTable_.comicId, comicId).build().findUnique()
                if (shelfBook != null) {
                    ComicResponse.success(shelfBook)
                }else {
                    ComicResponse.error("未加入书架")
                }
            }
        }
    }

    suspend fun addBookToShelf(comicId: String, comicName: String, coverUrl: String, chapterSize: Int) : ComicResponse<BookShelfTable>{
        return launchIO {
            ComicObjectBox.bookShelfBox.run {
                var comicBook = query().equal(BookShelfTable_.comicId, comicId).build().findUnique()
                if (comicBook == null) {
                    comicBook = BookShelfTable(comicId, comicName, coverUrl, 0, chapterSize, System.currentTimeMillis())
                    val id = put(comicBook)
                    comicBook.id = id
                }
                ComicResponse.success(comicBook)
            }
        }
    }

    suspend fun removeBookFromShelf(comicId: String) : ComicResponse<Boolean>{
        return launchIO {
            ComicObjectBox.bookShelfBox.run {
                query().equal(BookShelfTable_.comicId, comicId).build().findUnique()?.let {
                    remove(it)
                }
                ComicResponse.success(true)
            }
        }
    }


    suspend fun updateBookShelf(comicId: String, position: Int, chapterSize: Int): ComicResponse<Boolean>{
        return launchIO {
            ComicObjectBox.bookShelfBox.run {
                val comicBook = query().equal(BookShelfTable_.comicId, comicId).build().findUnique()
                if (comicBook == null) {
                    ComicResponse.error("记录未找到")
                }else {
                    if (chapterSize > comicBook.chapterSize) {
                        comicBook.chapterSize = chapterSize
                    }
                    comicBook.readChapterPosition = position
                    comicBook.readTime = System.currentTimeMillis()
                    put(comicBook)
                    ComicResponse.success(true)
                }
            }
        }
    }

    suspend fun comicPreView(chapterId: String): ComicResponse<ComicPreViewResponse> {
        return launchIO {
            apiService.comicPreView(chapterId)
        }
    }


}