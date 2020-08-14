package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.guochat.model.rep.ChatDetailRepository
import com.benyq.mvvm.mvvm.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatDetailViewModel@ViewModelInject constructor(private val mRepository: ChatDetailRepository) : BaseViewModel(){

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