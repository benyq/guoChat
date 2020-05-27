package com.benyq.guochat.local.entity

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
     * 图片地址
     */
    var imgUrl: String = "",
    /**
     * 语音地址
     */
    var voiceRecordPath: String = "",
    /**
     * 语音时长
     */
    var voiceRecordDuration: Long = 0L,
    /**
     * 视频地址
     */
    var videoPath: String = "",
    /**
     * 视频时长
     */
    var videoDuration: Long = 0L,
    /**
     * 类型
     */
    var chatType: Int = TYPE_TEXT,
    /**
     * 发送者id
     */
    var fromUid: Int = 0,
    /**
     * 接收者id
     */
    var toUid: Int = 0,

    var fromToId: Long = 0

) {

    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_IMG = 2
        const val TYPE_VOICE = 3
        const val TYPE_VIDEO = 4
    }


}

