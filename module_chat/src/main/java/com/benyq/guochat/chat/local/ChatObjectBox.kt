package com.benyq.guochat.chat.local

import com.benyq.guochat.chat.app.CHAT_TYPE_CONTRACT
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.database.DataObjectBox
import com.benyq.guochat.database.entity.chat.*
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query
import io.objectbox.query.QueryBuilder.StringOrder


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
    fun getConversationId(from: String, to: String): ConversationEntity {
        val chatBox: Box<ConversationEntity> = boxStore.boxFor()
        var conversation = chatBox.query {
            equal(ConversationEntity_.fromUid, from, StringOrder.CASE_INSENSITIVE)
            equal(ConversationEntity_.toUid, to, StringOrder.CASE_INSENSITIVE)

        }.findFirst()
        if (conversation == null) {
            conversation = ConversationEntity(fromUid = from, toUid = to)
            conversation.id = chatBox.put(conversation)
        }
        return conversation
    }

    /**
     * 具体和聊天对象的聊天记录
     */
    fun getChatRecord(chatId: Long, page: Long, size: Long): List<ChatRecordEntity> {
        val chatRecordBox: Box<ChatRecordEntity> = boxStore.boxFor()
        val data =  chatRecordBox.query {
            equal(ChatRecordEntity_.conversationId, chatId)
            orderDesc(ChatRecordEntity_.sendTime)
        }.find((page - 1) * size, size)
        data.reverse()
        return data
    }

    fun updateChatRecord(conversationId: Long, updateTime: Long = System.currentTimeMillis()) {
        val chatBox: Box<ConversationEntity> = boxStore.boxFor()
        val chatRecord: ConversationEntity? = chatBox.get(conversationId)
        chatRecord?.let {
            it.updateTime = updateTime
            chatBox.put(it)
        }
    }

    /**
     * 聊天记录已阅读
     */
    fun chatRecordHaveRead(conversationId: Long) {
        val chatBox: Box<ChatRecordEntity> = boxStore.boxFor()
        val data = chatBox.query().equal(ChatRecordEntity_.conversationId, conversationId)
            .equal(ChatRecordEntity_.isRead, false)
            .build().find()
        data.forEach {
            it.isRead = true
        }
        chatBox.put(data)
    }

    /**
     * 首页的聊天记录
     */
    fun getChatContracts(uid: String): List<ChatListBean> {
        val chatBox: Box<ConversationEntity> = boxStore.boxFor()
        val contractBox: Box<ContractEntity> = boxStore.boxFor()

        val chatRecordBox: Box<ChatRecordEntity> = boxStore.boxFor()
        val conversations = chatBox.query {
            equal(ConversationEntity_.fromUid, uid, StringOrder.CASE_INSENSITIVE)
            orderDesc(ConversationEntity_.updateTime)
        }.find()
        val chatListBeans = mutableListOf<ChatListBean>()
        conversations.forEach {
            val chatRecord = chatRecordBox.query()
                .equal(ChatRecordEntity_.conversationId, it.id)
                .orderDesc(ChatRecordEntity_.id)
                .build().findFirst()

            val contractEntity = contractBox.query().equal(ContractEntity_.contractId, it.toUid, StringOrder.CASE_INSENSITIVE)
                .build().findFirst()

            val lastConversion: String = chatRecord?.let { record ->
                when(record.chatType) {
                    ChatRecordEntity.TYPE_FILE -> {
                        "[文件]"
                    }
                    ChatRecordEntity.TYPE_VOICE -> {
                        "[语音]"
                    }
                    ChatRecordEntity.TYPE_VIDEO -> {
                        "[视频]"
                    }
                    ChatRecordEntity.TYPE_IMG -> {
                        "[图片]"
                    }
                    else -> {
                        record.content
                    }
                }
            } ?: ""
            val unreadRecord = chatRecordBox.query()
                .equal(ChatRecordEntity_.conversationId, it.id)
                .equal(ChatRecordEntity_.isRead, false)
                .build().count()
            if (lastConversion.isNotEmpty()) {
                chatListBeans.add(
                    ChatListBean(
                        contractEntity?.avatarUrl ?: "",
                        contractEntity?.nick ?: "",
                        contractEntity?.chatType ?: CHAT_TYPE_CONTRACT,
                        it.updateTime,
                        lastConversion,
                        true,
                        conversationId = it.id,
                        unreadRecord
                    )
                )
            }
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
            equal(ContractEntity_.ownUserId, uid, StringOrder.CASE_INSENSITIVE)
        }.find()
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

    fun getContractByConversation(conversationId: Long) : ContractBean? {
        val contractBox: Box<ContractEntity> = boxStore.boxFor()
        val conversationBox: Box<ConversationEntity> = boxStore.boxFor()

        val conversationEntity = conversationBox.query {
            equal(ConversationEntity_.id, conversationId)
        }.findUnique() ?: return null
        val contractEntity = contractBox.query {
            equal(ContractEntity_.contractId, conversationEntity.toUid, StringOrder.CASE_INSENSITIVE)
        }.findUnique()
        return if (contractEntity == null) {
            null
        }else {
            ContractBean(0, null, contractEntity.contractId, contractEntity.chatNo, contractEntity.nick, contractEntity.remark, contractEntity.gender, contractEntity.avatarUrl)
        }
    }

    fun searchContractById(uid: String, contractId: String): ContractEntity? {
        val contractBox: Box<ContractEntity> = boxStore.boxFor()
        return contractBox.query()
            .equal(ContractEntity_.ownUserId, uid, StringOrder.CASE_INSENSITIVE)
            .equal(ContractEntity_.contractId, contractId, StringOrder.CASE_INSENSITIVE)
            .build().findUnique()
    }

}