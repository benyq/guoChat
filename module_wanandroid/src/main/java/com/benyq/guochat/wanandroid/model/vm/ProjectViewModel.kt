package com.benyq.guochat.wanandroid.model.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.ProjectTreeData
import com.benyq.guochat.wanandroid.model.repository.ProjectRepository
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
class ProjectViewModel @Inject constructor(val repository: ProjectRepository) : BaseViewModel(){

    private val projectsTabData = MutableLiveData<List<ProjectTreeData>>()
    var selectedCid = MutableLiveData<String>()

    init {
        listProjectsTab()
    }

    private fun listProjectsTab() {
        quickLaunch<List<ProjectTreeData>> {
            onSuccess {
                projectsTabData.value = it
                if (it != null && it.isNotEmpty()) {
                    selectedCid.value = it[0].id
                }
            }
            onError {
                Logger.e("listProjectsTab error: ${it.message}")
            }
            request {
                repository.listProjectsTab()
            }
        }
    }

    private val page: Int = 1

    val pager = Pager(
        PagingConfig(
        pageSize = 20, prefetchDistance = 2, enablePlaceholders = true
    )
    ) {
        ProjectDataSource(this)
    }.flow.cachedIn(viewModelScope)

    fun changeCategory() {
        projectsTabData.value?.let {
            selectedCid.value = it[(it.indices).random()].id
        }
    }
}


class ProjectDataSource(private val viewModel: ProjectViewModel): PagingSource<Int, ArticleData>() {

    override fun getRefreshKey(state: PagingState<Int, ArticleData>) = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ArticleData> {
        val loadPage = params.key ?: 0
        val result = viewModel.repository.listProjects(loadPage, viewModel.selectedCid.value ?: "")
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