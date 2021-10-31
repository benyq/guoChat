package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.guochat.chat.model.rep.ChatRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note 聊天列表
 */
@HiltViewModel
class ChatViewModel @Inject constructor(private val mRepository: ChatRepository) : BaseViewModel() {

    val mChatListData = MutableLiveData<List<ChatListBean>>()

    fun getChatContracts() {
        quickLaunch<List<ChatListBean>> {
            request { mRepository.getChatContracts(ChatLocalStorage.uid) }
            onSuccess {
                mChatListData.value = it
            }
        }
    }
}