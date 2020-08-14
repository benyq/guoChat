package com.benyq.guochat.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author benyq
 * @time 2020/8/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class SharedViewModel : ViewModel(){

    //CHatFragment的聊天记录要改变
    private val _chatChange = MutableLiveData<Boolean>()
    val chatChange: LiveData<Boolean> = _chatChange

    fun notifyChatChange(flag: Boolean) {
        if (!flag) {
            return
        }
        _chatChange.value = true
    }
}