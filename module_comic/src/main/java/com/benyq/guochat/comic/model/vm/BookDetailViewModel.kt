package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.bean.ComicDetailResponse
import com.benyq.guochat.comic.model.repository.BookDetailRepository
import com.benyq.guochat.database.entity.comic.BookShelfTable
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class BookDetailViewModel @ViewModelInject constructor(private val repository: BookDetailRepository) :
    BaseViewModel() {

    val bookDetailResult = MutableLiveData<UiState<ComicDetailResponse>>()
    val bookShelfResult = MutableLiveData<BookShelfTable>()

    // true 加入书架
    val addOrRemoveResult = MutableLiveData<Boolean>()

    fun getComicDetail(comicId: String) {
        quickLaunch<ComicDetailResponse> {

            onSuccess { bookDetailResult.value = UiState(isSuccess = it) }

            request { repository.getComicDetail(comicId) }
        }
    }

    fun searchBookShelf(comicId: String) {
        quickLaunch<BookShelfTable> {

            onSuccess { bookShelfResult.value = it }

            onError { }

            request { repository.searchBookShelf(comicId) }
        }
    }

    fun addBookToShelf(comicId: String, comicName: String, coverUrl: String, chapterSize: Int) {
        quickLaunch<BookShelfTable> {
            onSuccess {
                bookShelfResult.value = it
                addOrRemoveResult.value = true
            }
            onError {
                Toasts.show("加入书架失败${it.message}")
            }
            request { repository.addBookToShelf(comicId, comicName, coverUrl, chapterSize) }
        }
    }

    fun removeBookFromShelf(comicId: String) {
        quickLaunch<Boolean> {

            onSuccess {
                addOrRemoveResult.value = false
            }
            onError {
                Toasts.show("移除书架失败${it.message}")
            }
            request { repository.removeBookFromShelf(comicId) }
        }
    }

    fun updateBookShelf(comicId: String, position: Int, chapterSize: Int) {
        quickLaunch<Boolean> {
            onSuccess { _ ->
                val comicBook = bookShelfResult.value
                comicBook?.let {
                    it.chapterSize = chapterSize
                    it.readChapterPosition = position
                    bookShelfResult.value = it
                }
            }
            onError { }
            request { repository.updateBookShelf(comicId, position, chapterSize) }
        }
    }
}