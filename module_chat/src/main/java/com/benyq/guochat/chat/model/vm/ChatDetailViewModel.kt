package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.model.rep.ChatDetailRepository
import com.benyq.guochat.database.entity.chat.ChatRecordEntity
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class ChatDetailViewModel @Inject constructor(private val mRepository: ChatDetailRepository) :
    BaseViewModel() {

    val mChatRecordData = MutableLiveData<List<ChatRecordEntity>>()
    val mSendMessageData = MutableLiveData<Boolean>()

    val mChatMoreRecordData = MutableLiveData<List<ChatRecordEntity>>()
    val mChatLatestRecordData = MutableLiveData<List<ChatRecordEntity>>()

    var requestPage = 1L
    val pageSize = 20L

    fun getChatRecord(conversationId: Long) {
        requestPage = 1L
        quickLaunch<List<ChatRecordEntity>> {
            onSuccess {
                mChatRecordData.value = it
            }
            request { mRepository.getChatRecord(conversationId, requestPage, pageSize) }
        }
    }


    fun requestMoreRecord(conversationId: Long, firstRecordTime: Long) {
        requestPage++
        quickLaunch<List<ChatRecordEntity>> {

            onStart { showLoading("") }
            onFinal { hideLoading() }

            onSuccess {
                mChatMoreRecordData.value = it
            }
            request { mRepository.requestMoreRecord(conversationId, firstRecordTime, requestPage, pageSize) }
        }
    }

    //ui中最后一条记录的id
    fun requestLatestRecord(conversationId: Long, currentRecordTime: Long) {
        quickLaunch<List<ChatRecordEntity>> {
            onSuccess {
                mChatLatestRecordData.value = it
            }
            request { mRepository.requestLatestRecord(conversationId, currentRecordTime) }
        }
    }

    fun sendChatMessage(data: ChatRecordEntity) {
        data.isRead = true
        quickLaunch<Boolean> {
            request { mRepository.sendChatMessage(data) }
            onSuccess {
                mSendMessageData.value = it
            }
        }
    }
}