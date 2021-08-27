package com.benyq.guochat.wanandroid.model.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.BannerData
import com.benyq.guochat.wanandroid.model.http.PageData
import com.benyq.guochat.wanandroid.model.repository.MainRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2021/8/8
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : BaseViewModel(){
    val homeArticleData = MutableLiveData<List<ArticleData>>()
    val bannerData = MutableLiveData<List<BannerData>>()

    private val page: Int = 1

    fun getArticleData() {
        quickLaunch<PageData<ArticleData>> {
            onSuccess {
                it?.run {
                    if (it.over) {
                        //全部获取
                    }
                    val oldData = homeArticleData.value
                    if (oldData == null) {
                        homeArticleData.value = it.data
                    }else {
                        oldData.toMutableList().addAll(it.data)
                        homeArticleData.value = oldData.toList()
                    }
                }
            }
            onError {

            }

            request {
                repository.articleList(page)
            }
        }
    }

    val pager = Pager(PagingConfig(
        pageSize = 20, prefetchDistance = 2, enablePlaceholders = true
    )) {
        ArticleDataSource(repository)
    }.flow.cachedIn(viewModelScope)


    fun bannerData(){
        quickLaunch<List<BannerData>> {
            onSuccess {
                bannerData.value = it
            }
            onError {

            }
            request {
                repository.banner()
            }
        }
    }

}

class ArticleDataSource(private val repo: MainRepository): PagingSource<Int, ArticleData>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleData>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleData> {
        val loadPage = params.key ?: 0
        val result = repo.articleList(loadPage)
        val nextPage = if (result.getRealData() == null) null else loadPage + 1
        return if (result.data != null && loadPage < 2){
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