package com.benyq.guochat.chat.local

import android.content.Context
import com.benyq.guochat.chat.app.CHAT_TYPE_CONTRACT
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.guochat.database.DataObjectBox
import com.benyq.guochat.database.entity.chat.*
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query


/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */
object ChatObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init() {
        boxStore = DataObjectBox.boxStore
    }

    /**
     * 获取ChatFromToEntity，如果不存在则新建
     */
    fun getChatFromTo(from: Long, to: Long): ChatFromToEntity {
        val chatBox: Box<ChatFromToEntity> = boxStore.boxFor()
        var chatFromTo = chatBox.query {
            equal(ChatFromToEntity_.fromUid, from)
            equal(ChatFromToEntity_.toUid, to)
        }.findFirst()
        if (chatFromTo == null) {
            chatFromTo = ChatFromToEntity(fromUid = from, toUid = to)
            chatFromTo.id = chatBox.put(chatFromTo)
        }
        return chatFromTo
    }


    /**
     * 具体和聊天对象的聊天记录
     */
    fun getChatRecord(chatId: Long, page: Long, size: Long): List<ChatRecordEntity> {
        val chatRecordBox: Box<ChatRecordEntity> = boxStore.boxFor()
        val data =  chatRecordBox.query {
            equal(ChatRecordEntity_.fromToId, chatId)
            orderDesc(ChatRecordEntity_.sendTime)
        }.find((page - 1) * size, size)
        data.reverse()
        return data
    }

    fun updateChatRecord(chatId: Long) {
        val chatBox: Box<ChatFromToEntity> = boxStore.boxFor()
        val chatRecord: ChatFromToEntity? = chatBox.get(chatId)
        chatRecord?.let {
            it.updateTime = System.currentTimeMillis()
            chatBox.put(it)
        }
    }

    /**
     * 首页的聊天记录
     */
    fun getChatContracts(): List<ChatListBean> {
        val chatBox: Box<ChatFromToEntity> = boxStore.boxFor()
        val contractBox: Box<ContractEntity> = boxStore.boxFor()

        val chatRecordBox: Box<ChatRecordEntity> = boxStore.boxFor()
        val chatFromTos = chatBox.query {
            orderDesc(ChatFromToEntity_.updateTime)
        }.find()
        val chatListBeans = mutableListOf<ChatListBean>()
        chatFromTos.forEach {
            val chatRecord = chatRecordBox.query()
                .equal(ChatRecordEntity_.fromToId, it.id)
                .orderDesc(ChatRecordEntity_.id)
                .build().findFirst()

            val contractEntity = contractBox.query().equal(ContractEntity_.id, it.toUid)
                .build().findFirst()

            val lastConversion: String = chatRecord?.let { record ->
                when {
                    record.content.isNotEmpty() -> {
                        record.content
                    }
                    record.voiceRecordPath.isNotEmpty() -> {
                        "[语音]"
                    }
                    record.videoPath.isNotEmpty() -> {
                        "[视频]"
                    }
                    else -> {
                        "[图片]"
                    }
                }
            } ?: ""
            chatListBeans.add(
                ChatListBean(
                    contractEntity?.avatarUrl ?: "",
                    contractEntity?.nick ?: "",
                    contractEntity?.chatType ?: CHAT_TYPE_CONTRACT,
                    it.updateTime,
                    lastConversion,
                    true,
                    fromToId = it.id
                )
            )
        }
        return chatListBeans
    }

    fun saveChatMessage(data: ChatRecordEntity): Long {
        val messageBox: Box<ChatRecordEntity> = boxStore.boxFor()
        return messageBox.put(data)
    }

    /**
     * uid,当前用户id
     */
    fun getAllContracts(uid: String): List<ContractEntity> {
        val contractStore: Box<ContractEntity> = boxStore.boxFor()
        return contractStore.query{
            equal(ContractEntity_.ownUserId, uid)
        }.find()
    }

    /**
     * @param fromUid 服务器中的uid
     * @param toUid 本地数据库中的联系人uid
     */
    fun findFromToByIds(fromUid: String, toUid: Long): ChatListBean {
        val contractBox: Box<ContractEntity> = boxStore.boxFor()
        val contractEntity = contractBox.query().equal(ContractEntity_.id, toUid)
            .equal(ContractEntity_.ownUserId, fromUid)
            .build().findFirst()
        return ChatListBean(
            contractEntity?.avatarUrl ?: "",
            contractEntity?.nick ?: "",
            contractEntity?.chatType ?: CHAT_TYPE_CONTRACT,
            0L,
            "",
            true,
            toUid
        )
    }

    /**
     * @param id 本地数据库中的联系人id
     */
    fun deleteChatRecordByContractId(id: Long) {
        val chatRecordBox: Box<ChatRecordEntity> = boxStore.boxFor()
        chatRecordBox.query {
            equal(ChatRecordEntity_.fromUid, id)
        }.remove()
        chatRecordBox.query {
            equal(ChatRecordEntity_.toUid, id)
        }.remove()
    }
}