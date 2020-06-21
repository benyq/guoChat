package com.benyq.guochat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.guochat.model.rep.ChatDetailRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatDetailViewModel : BaseViewModel<ChatDetailRepository>(){

    val mChatRecordData = MutableLiveData<List<ChatRecordEntity>>()
    val mSendMessageData = MutableLiveData<Boolean>()

    fun getChatRecord(chatId: Long, page: Int, size: Int) {
        quickLaunch<List<ChatRecordEntity>> {
            onSuccess {
                mChatRecordData.value = it
            }
            request { mRepository.getChatRecord(chatId, page, size) }
        }
    }


    fun sendChatMessage(data: ChatRecordEntity) {
        quickLaunch<Boolean> {
            request { mRepository.sendChatMessage(data) }
            onSuccess {
                mSendMessageData.value = it
            }
        }
    }
}