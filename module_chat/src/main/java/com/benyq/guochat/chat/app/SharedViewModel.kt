package com.benyq.guochat.chat.app

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.benyq.guochat.chat.function.message.WebSocketManager
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.ui.chats.ChatDetailActivity
import com.benyq.guochat.database.entity.chat.ConversationEntity
import com.benyq.guochat.media.NotificationHelper
import com.benyq.module_base.ext.loge

/**
 * @author benyq
 * @time 2020/8/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class SharedViewModel(private val app: Application) : AndroidViewModel(app){

    //ChatFragment、ChatDetailActivity 的聊天记录要改变
    private val _chatChange = MutableLiveData<Long>()
    val chatChange: LiveData<Long> = _chatChange

    var conversationId = -1L

    fun notifyChatChange(conversationId: Long, contract: ContractBean, msg: String) {
        if (this.conversationId != conversationId) {
            val intent = Intent(app, ChatDetailActivity::class.java).apply {
                putExtra(IntentExtra.conversationId, conversationId)
            }
            NotificationHelper.showMessageNotification(app, contract.nick, msg, intent)
        }

        _chatChange.postValue(conversationId)
    }

    fun reset() {
        conversationId = -1L
    }



    val personInfoChange = MutableLiveData<Boolean>()

    fun notifyPersonInfoChange() {
        personInfoChange.value = true
    }
}