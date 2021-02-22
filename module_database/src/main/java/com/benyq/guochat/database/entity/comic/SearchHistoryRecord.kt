package com.benyq.guochat.database.entity.comic

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
@Entity
data class SearchHistoryRecord(
    @Unique
    val comicId: String, val name: String, var updateTime: Long, @Id var id: Long = 0
)