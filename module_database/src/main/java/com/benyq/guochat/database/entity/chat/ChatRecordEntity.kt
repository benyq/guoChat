package com.benyq.guochat.database.entity.chat

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id


/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note 对话内容，可能是文字，图片或者语音，
 */
@Entity
data class ChatRecordEntity(
    @Id var id: Long = 0,
    /**
     * 内容
     */
    var content: String = "",
    /**
     * 发送时间
     */
    var sendTime: Long = System.currentTimeMillis(),
    /**
     * 文件地址, 包括 图片、视频、普通文件
     */
    var filePath: String = "",
    /**
     * 视频或者音频时长
     */
    var duration: Long = 0L,
    /**
     * 类型
     */
    var chatType: Int = TYPE_TEXT,
    /**
     * 发送者id
     */
    var fromUid: String,
    /**
     * 接收者id
     */
    var toUid: String,
    /**
     * 会话Id
     */
    var conversationId: Long = 0
) {

    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_FILE  = 2
        const val TYPE_IMG  = 3
        const val TYPE_VIDEO = 4
        const val TYPE_VOICE = 5
    }


}

