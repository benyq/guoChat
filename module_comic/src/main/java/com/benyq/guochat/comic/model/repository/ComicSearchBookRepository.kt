package com.benyq.guochat.comic.model.repository

import com.benyq.guochat.comic.local.ComicObjectBox
import com.benyq.guochat.comic.local.SearchHistoryRecord
import com.benyq.guochat.comic.local.SearchHistoryRecord_
import com.benyq.guochat.comic.model.bean.ComicSearchResponse
import com.benyq.guochat.comic.model.bean.SearchHotEntity
import com.benyq.guochat.comic.model.http.ComicApiService
import com.benyq.guochat.comic.model.http.ComicResponse
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchBookRepository @Inject constructor(private val apiService: ComicApiService): BaseRepository(){
    suspend fun comicSearchHot(): ComicResponse<SearchHotEntity> {
        return launchIO {
            apiService.comicSearchHot()
        }
    }

    suspend fun addSearchHistory(comicId: String, name: String): ComicResponse<Boolean>{
        return launchIO {
            ComicObjectBox.searchHistoryBox.run {
                var searchRecord = query().equal(SearchHistoryRecord_.comicId, comicId).build().findUnique()
                if (searchRecord == null) {
                    searchRecord = SearchHistoryRecord(comicId, name, System.currentTimeMillis())
                }else {
                    searchRecord.updateTime = System.currentTimeMillis()
                }
                put(searchRecord)

                val count = query().build().count()
                if (count >= ComicObjectBox.searchHistoryLimit) {
                    val invalidData = query().orderDesc(SearchHistoryRecord_.updateTime).build().find(ComicObjectBox.searchHistoryLimit, count)
                    remove(invalidData)
                }
                ComicResponse.success(true)
            }
        }
    }

    suspend fun getSearchHistory(): ComicResponse<List<SearchHistoryRecord>> {
        return launchIO {
            ComicObjectBox.searchHistoryBox.run {
                val historyRecords = query().orderDesc(SearchHistoryRecord_.updateTime).build().find()
                ComicResponse.success(historyRecords)
            }
        }
    }

    suspend fun deleteHistoryRecord(id: Long): ComicResponse<Boolean>{
        return launchIO {
            ComicObjectBox.searchHistoryBox.remove(id)
            ComicResponse.success(true)
        }
    }

    suspend fun comicSearch(text: String, page: Int): ComicResponse<ComicSearchResponse>{
        return launchIO {
            apiService.comicSearch(text, page)
        }
    }
}