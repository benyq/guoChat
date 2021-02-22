package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.bean.ComicSearchResponse
import com.benyq.guochat.comic.model.bean.SearchHotEntity
import com.benyq.guochat.comic.model.repository.ComicSearchBookRepository
import com.benyq.guochat.database.entity.comic.SearchHistoryRecord
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchBookViewModel @ViewModelInject constructor(private val repository: ComicSearchBookRepository): BaseViewModel(){

    val searchHotKey = MutableLiveData<SearchHotEntity>()
    val searchHistory = MutableLiveData<List<SearchHistoryRecord>>()
    val deleteHistoryResult = MutableLiveData<Int>()
    val comicSearchResult = MutableLiveData<UiState<ComicSearchResponse>>()

    private var currentPage = 1
    private var currentSearchKey  = ""

    fun comicSearchHot(){
        quickLaunch<SearchHotEntity> {

            onSuccess { searchHotKey.value = it }

            request { repository.comicSearchHot() }
        }
    }

    fun getSearchHistory(){
        quickLaunch<List<SearchHistoryRecord>> {

            onSuccess { searchHistory.value = it }

            request { repository.getSearchHistory() }
        }
    }

    fun addSearchHistory(comicId: String, name: String) {
        quickLaunch<Boolean> {

            onSuccess { getSearchHistory() }

            request { repository.addSearchHistory(comicId, name) }
        }
    }

    fun deleteHistoryRecord(id: Long, position: Int) {

        quickLaunch<Boolean> {

            onSuccess {
                if (it == true) {
                    deleteHistoryResult.value = position
                }
            }

            request { repository.deleteHistoryRecord(id) }
        }
    }

    fun comicSearch(text: String, page: Int = 1) {
        currentSearchKey = text
        quickLaunch<ComicSearchResponse> {

            onSuccess {
                comicSearchResult.value = UiState(isSuccess = it)
                currentPage = (it?.page ?: 1) + 1
            }

            onError { comicSearchResult.value = UiState(isError = it) }

            request { repository.comicSearch(text, page) }
        }
    }

    fun comicSearchMore() {
        comicSearch(currentSearchKey, currentPage)
    }
}