package com.benyq.guochat.database.entity.chat

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note 保存 聊天对象信息
 */
@Entity
data class ChatFromToEntity(
    @Id var id: Long = 0,
    var fromUid: Long = 0,
    var toUid: Long = 0,
    var updateTime: Long = System.currentTimeMillis()
)