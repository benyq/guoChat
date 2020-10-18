package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.model.bean.ComicDetailResponse
import com.benyq.guochat.comic.model.bean.ComicPreViewResponse
import com.benyq.guochat.comic.model.repository.BookDetailRepository
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note
 */
class ReadComicBookViewModel @ViewModelInject constructor(private val repository: BookDetailRepository) : BaseViewModel(){

    val previewResult = MutableLiveData<UiState<ComicPreViewResponse>>()
    val bookDetailResult = MutableLiveData<UiState<ComicDetailResponse>>()


    fun comicPreView(chapterId: String) {
        quickLaunch<ComicPreViewResponse> {

            onStart {
                previewResult.value = UiState(isLoading = true)
            }

            onSuccess { previewResult.value = UiState(isSuccess = it) }

            onError { previewResult.value = UiState(isLoading = false, isError = it.message) }

            request { repository.comicPreView(chapterId) }
        }
    }

    /**
     * @param chapterSize 保存时会判断，只保留最大的
     */
    fun updateBookShelf(comicId: String, position: Int, chapterSize: Int) {
        quickLaunch<Boolean> {
            onSuccess {
                loge("updateBookShelf onSuccess $it")
            }
            onError {
                loge("updateBookShelf onError ${it.message}")

            }
            request { repository.updateBookShelf(comicId, position, chapterSize) }
        }
    }


    fun getComicDetail(comicId: String) {
        quickLaunch<ComicDetailResponse> {

            onStart {
                previewResult.value = UiState(isLoading = true)
            }

            onSuccess { bookDetailResult.value = UiState(isSuccess = it, isLoading = true) }

            request { repository.getComicDetail(comicId) }
        }
    }

}