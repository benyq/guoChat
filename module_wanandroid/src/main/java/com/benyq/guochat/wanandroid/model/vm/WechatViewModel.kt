package com.benyq.guochat.wanandroid.model.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.WechatAuthorData
import com.benyq.guochat.wanandroid.model.repository.WechatRepository
import com.benyq.module_base.mvvm.BaseViewModel
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @author benyq
 * @date 2021/8/30
 * @email 1520063035@qq.com
 *
 */
@HiltViewModel
class WechatViewModel @Inject constructor(val repository: WechatRepository) : BaseViewModel(){

    private val wechatAuthorData = MutableLiveData<List<WechatAuthorData>>()
    var selectedAuthor = MutableLiveData<WechatAuthorData>()

    init {
        listWechatAuthor()
    }

    private fun listWechatAuthor() {
        quickLaunch<List<WechatAuthorData>> {
            onSuccess {
                wechatAuthorData.value = it
                if (it != null && it.isNotEmpty()) {
                    selectedAuthor.value = it[0]
                }
            }
            onError {
                Logger.e("listProjectsTab error: ${it.message}")
            }
            request {
                repository.listWechatAuthor()
            }
        }
    }

    val pager = Pager(
        PagingConfig(
        pageSize = 20, prefetchDistance = 2, enablePlaceholders = true
    )
    ) {
        WechatDataSource(this)
    }.flow.cachedIn(viewModelScope)


    fun changeWechatAuthor() {
        wechatAuthorData.value?.let {
            selectedAuthor.value = it[(it.indices).random()]
        }
    }
}


class WechatDataSource(private val viewModel: WechatViewModel): PagingSource<Int, ArticleData>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleData>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleData> {
        val loadPage = params.key ?: 0
        val result = viewModel.repository.listWechatArticle(loadPage, viewModel.selectedAuthor.value?.id ?: "")
        val nextPage = if (result.data == null) null else loadPage + 1
        return if (result.data != null){
            LoadResult.Page(
                data = result.data.data,
                prevKey = null,
                nextKey = nextPage
            )
        } else {
            LoadResult.Error(throwable = Throwable())
        }
    }

}