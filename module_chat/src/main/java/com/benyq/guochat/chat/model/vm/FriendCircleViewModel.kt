package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.model.bean.FriendCircleBean
import com.benyq.guochat.chat.model.rep.FriendCircleRepository
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/7/11
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class FriendCircleViewModel @Inject constructor(private val mRepository: FriendCircleRepository) :
    BaseViewModel() {

    val mFriendCircleData = MutableLiveData<List<FriendCircleBean>>()
    val mFriendCircleLikeData = MutableLiveData<Boolean>()

    fun queryFriendCircles() {
        quickLaunch<List<FriendCircleBean>> {
            onError { Toasts.show("请求失败") }
            onSuccess { mFriendCircleData.value = it!!.toMutableList().apply { addAll(it) } }
            request { mRepository.queryFriendCircles() }
        }
    }

    fun friendCircleLike(circleId: String, like: Boolean) {
        quickLaunch<Boolean> {
            onError { Toasts.show("请求失败") }
            onSuccess { mFriendCircleLikeData.value = it }
            request { mRepository.friendCircleLike(like) }
        }
    }
}