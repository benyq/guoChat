package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.bean.ComicPreViewResponse
import com.benyq.guochat.comic.model.repository.ReadComicBookRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note
 */
class ReadComicBookViewModel @ViewModelInject constructor(private val repository: ReadComicBookRepository) : BaseViewModel(){

    val previewResult = MutableLiveData<UiState<ComicPreViewResponse>>()

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
}