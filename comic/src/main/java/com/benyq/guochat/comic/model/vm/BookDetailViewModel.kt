package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.bean.ComicDetailResponse
import com.benyq.guochat.comic.model.repository.BookDetailRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class BookDetailViewModel @ViewModelInject constructor(private val repository: BookDetailRepository): BaseViewModel(){

    val bookDetailResult = MutableLiveData<UiState<ComicDetailResponse>>()

    fun getComicDetail(comicId: String) {
        quickLaunch<ComicDetailResponse> {

            onSuccess { bookDetailResult.value = UiState(isSuccess = it) }

            request { repository.getComicDetail(comicId) }
        }
    }
}