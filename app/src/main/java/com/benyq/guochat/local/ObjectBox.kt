package com.benyq.guochat.local

import android.content.Context
import com.benyq.guochat.app.CHAT_TYPE_CONTRACT
import com.benyq.guochat.local.entity.*
import com.benyq.guochat.model.bean.ChatListBean
import com.benyq.mvvm.ext.logi
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.android.BuildConfig
import io.objectbox.kotlin.boxFor
import io.objectbox.kotlin.query


/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */
object ObjectBox {

    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext).build()

        if (!BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(context)
            logi("ObjectBrowser Started: $started")
        }
    }

    fun testAddChatFromTo() {
        val chatBox: Box<ChatFromToEntity> = boxStore.boxFor()
        val contractBox: Box<ContractEntity> = boxStore.boxFor()
        if (chatBox.query().build().count() <= 0) {
            chatBox.put(
                ChatFromToEntity(
                    0,
                    0L,
                    1L,
                    System.currentTimeMillis()
                ),
                ChatFromToEntity(
                    0,
                    0L,
                    2L,
                    System.currentTimeMillis()
                ),
                ChatFromToEntity(
                    0,
                    0L,
                    3L,
                    System.currentTimeMillis()
                ),
                ChatFromToEntity(
                    0,
                    0L,
                    4L,
                    System.currentTimeMillis()
                )
            )
        }
        if (contractBox.query().build().count() <= 0) {
            contractBox.put(
                ContractEntity(
                    0, "2", "klfjjasjasjda", "哪吒", 1, "哪吒",
                    "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1305353222,2352820043&fm=26&gp=0.jpg",
                    CHAT_TYPE_CONTRACT
                ),
                ContractEntity(
                    0, "2", "klfjjasjasjda", "三公主", 0, "三公主",
                    "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3170379310,1742401393&fm=111&gp=0.jpg",
                    CHAT_TYPE_CONTRACT
                ),
                ContractEntity(
                    0, "2", "klfjjasjasjda", "招新", 2, "招新",
                    "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1361981880,3052617388&fm=111&gp=0.jpg",
                    CHAT_TYPE_CONTRACT
                ),
                ContractEntity(
                    0, "2", "klfjjasjasjda", "凯南", 1, "凯南",
                    "https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=4156830825,3265157570&fm=111&gp=0.jpg",
                    CHAT_TYPE_CONTRACT
                )
            )
        }
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
        val chatRecords = chatRecordBox.query {
            equal(ChatRecordEntity_.fromToId, chatId)
            order(ChatRecordEntity_.sendTime)
        }.find((page - 1) * size, size)
        return chatRecords
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

    fun getAllContracts(): List<ContractEntity> {
        val contractStore: Box<ContractEntity> = boxStore.boxFor()
        return contractStore.query().build().find()
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
}