package com.benyq.guochat.wanandroid.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.wanandroid.model.ArticleData
import com.benyq.guochat.wanandroid.model.BannerData
import com.benyq.guochat.wanandroid.model.PageData
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