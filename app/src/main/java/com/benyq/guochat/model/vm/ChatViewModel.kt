package com.benyq.guochat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.bean.ChatListBean
import com.benyq.guochat.model.rep.ChatRepository
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note 聊天列表
 */
class ChatViewModel(private val mRepository: ChatRepository) : BaseViewModel(){

    val mChatListData = MutableLiveData<List<ChatListBean>>()

    fun getChatContracts() {
        quickLaunch<List<ChatListBean>> {
            request { mRepository.getChatContracts() }
            onSuccess {
                mChatListData.value = it
            }
        }
    }
}