package com.benyq.guochat.chat.model.rep

import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.database.entity.chat.ChatRecordEntity
import com.benyq.module_base.mvvm.BaseRepository
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatDetailRepository @Inject constructor() : BaseRepository(){

    suspend fun getChatRecord(chatId: Long, page: Int, size: Int): ChatResponse<List<ChatRecordEntity>> {
        return launchIO {
            ChatResponse.success(ChatObjectBox.getChatRecord(chatId, page.toLong(), size.toLong()))
        }
    }

    suspend fun sendChatMessage(data: ChatRecordEntity) : ChatResponse<Boolean>{
        return launchIO {
            val id = ChatObjectBox.saveChatMessage(data)
            if (id > 0) {
                ChatObjectBox.updateChatRecord(data.fromToId)
                ChatResponse.success(true)
            }else {
                ChatResponse.success(false)
            }
        }
    }
}