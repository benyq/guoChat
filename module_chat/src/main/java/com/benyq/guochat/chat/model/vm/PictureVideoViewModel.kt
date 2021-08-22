package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note 只是在PictureVideoActivity 与 Fragment之间传递消息的
 */
@HiltViewModel
class PictureVideoViewModel @Inject constructor() : ViewModel() {

    val mState = MutableLiveData<StateEvent>()

    fun showPictureConfirm(path: String) {
        mState.postValue(StateEvent(StateEvent.STATE_IMG, path))
    }

    fun showVideoConfirm(path: String, videoDuration: Int) {
        mState.postValue(StateEvent(StateEvent.STATE_VIDEO, path, videoDuration))
    }

    fun clearTop() {
        mState.postValue(StateEvent(StateEvent.STATE_CLEAR_TOP))
    }

    fun clearAll() {
        mState.postValue(StateEvent(StateEvent.STATE_CLEAR_ALL))
    }

    fun finishImg(path: String) {
        mState.value = StateEvent(StateEvent.STATE_FINISH_IMG, path)
    }

    fun finishVideo(path: String, videoDuration: Int) {
        mState.value = StateEvent(StateEvent.STATE_FINISH_VIDEO, path, videoDuration)
    }
}

class StateEvent(val state: Int, val path: String? = null, var videoDuration: Int = 0) {

    companion object {
        const val STATE_IMG = 1
        const val STATE_VIDEO = 2
        const val STATE_CLEAR_TOP = 3
        const val STATE_CLEAR_ALL = 4

        //结果返回
        const val STATE_FINISH_IMG = 5
        const val STATE_FINISH_VIDEO = 6
    }
}