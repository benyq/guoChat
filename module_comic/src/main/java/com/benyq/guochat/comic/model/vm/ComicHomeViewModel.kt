package com.benyq.guochat.comic.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.benyq.guochat.comic.model.repository.ComicHomeRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class ComicHomeViewModel @Inject constructor(private val repository: ComicHomeRepository) :
    BaseViewModel() {

    val comicList = MutableLiveData<List<RecommendEntity.ComicLists>>()

    fun boutiqueList(sexTYpe: Int = 1) {
        quickLaunch<RecommendEntity> {
            onSuccess {
                comicList.value = it?.comicLists
            }

            onError {

            }

            request { repository.boutiqueList(sexTYpe) }
        }
    }
}