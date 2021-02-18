package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatRepository @Inject constructor() : BaseRepository(){

    suspend fun getChatContracts(): ChatResponse<List<ChatListBean>> {
        return launchIO {
            ChatResponse.success(ChatObjectBox.getChatContracts())
        }
    }
}