package com.benyq.guochat.chat.model.bean

/**
 * @author benyq
 * @time 2021/10/31
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ChatMessageBean(
    val fromId: String,
    val toId: String,
    val msg: String,
    val type: Int,
    val sendTime: Long,
)