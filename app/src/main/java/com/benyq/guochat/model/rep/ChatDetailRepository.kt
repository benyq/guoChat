package com.benyq.guochat.model.rep

import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.mvvm.mvvm.BaseRepository

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatDetailRepository : BaseRepository(){

    suspend fun getChatRecord(chatId: Long, page: Long, size: Long): ChatResponse<List<ChatRecordEntity>> {
        return launchIO {
            ChatResponse.success(ObjectBox.getChatRecord(chatId, page, size))
        }
    }

    suspend fun sendChatMessage(data: ChatRecordEntity) : ChatResponse<Boolean>{
        return launchIO {
            val id = ObjectBox.saveChatMessage(data)
            if (id > 0) {
                ObjectBox.updateChatRecord(data.fromToId)
                ChatResponse.success(true)
            }else {
                ChatResponse.success(false)
            }
        }
    }
}