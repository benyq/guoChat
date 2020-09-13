package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.guochat.model.net.OpenEyeService
import com.benyq.guochat.model.rep.OpenEyeRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
class OpenEyeRecommendViewModel @ViewModelInject constructor(private val mRepository: OpenEyeRepository): BaseViewModel() {

    val recommendDataResult = MutableLiveData<List<CommunityRecommend.Item>>()
    val recommendDataMoreResult = MutableLiveData<List<CommunityRecommend.Item>>()
    val bannerDataResult = MutableLiveData<List<CommunityRecommend.ItemX>>()

    var nextPageUrl: String? = null

    fun onRefresh() {
        requestData(OpenEyeService.COMMUNITY_RECOMMEND_URL)
    }

    fun onLoadMore() {
        requestData(nextPageUrl ?: "")
    }

    private fun requestData(url: String) {
        quickLaunch<CommunityRecommend> {
            onSuccess {
                it?.let { data ->
                    nextPageUrl = data.nextPageUrl
                    val cardData = mutableListOf<CommunityRecommend.Item>()
                    data.itemList.forEach {item ->
                        if (item.type == "horizontalScrollCard" && item.data.dataType == "HorizontalScrollCard") {
                            //banner 数据
                            item.data.itemList?.run{
                                bannerDataResult.value = this
                            }
                        }
                        if (item.type == "communityColumnsCard" && item.data.dataType == "FollowCard") {
                            //卡片
                            cardData.add(item)
                        }
                    }
                    if (cardData.isEmpty()) {
                        return@onSuccess
                    }
                    if (url == OpenEyeService.COMMUNITY_RECOMMEND_URL) {
                        //刷新
                        recommendDataResult.value = cardData
                    }else {
                        //更多
                        recommendDataMoreResult.value = cardData
                    }
                }
            }

            onFail {

            }

            request { mRepository.refreshCommunityRecommend(url) }
        }
    }
}