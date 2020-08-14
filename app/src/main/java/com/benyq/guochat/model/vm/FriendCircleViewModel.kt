package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.bean.FriendCircleBean
import com.benyq.guochat.model.rep.FriendCircleRepository
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note
 */
class FriendCircleViewModel @ViewModelInject constructor(private val mRepository: FriendCircleRepository) : BaseViewModel(){

    val mFriendCircleData = MutableLiveData<List<FriendCircleBean>>()
    val mFriendCircleLikeData = MutableLiveData<Boolean>()

    fun queryFriendCircles() {
        quickLaunch<List<FriendCircleBean>> {
            onException { Toasts.show("请求失败") }
            onSuccess { mFriendCircleData.value = it }
            request { mRepository.queryFriendCircles() }
        }
    }

    fun friendCircleLike(circleId: String, like: Boolean) {
        quickLaunch<Boolean> {
            onException { Toasts.show("请求失败") }
            onSuccess { mFriendCircleLikeData.value = it }
            request { mRepository.friendCircleLike(like) }
        }
    }
}