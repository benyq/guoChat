package com.benyq.guochat.database.entity.chat

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @author benyq
 * @time 2021/10/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@Entity
data class ConversationEntity(
    @Id
    var id: Long = 0,
    var fromUid: String,
    var toUid: String,
    var updateTime: Long = System.currentTimeMillis()
)