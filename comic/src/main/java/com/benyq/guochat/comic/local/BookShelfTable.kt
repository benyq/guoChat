package com.benyq.guochat.comic.local

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique

/**
 * @author benyq
 * @time 2020/10/11
 * @e-mail 1520063035@qq.com
 * @note
 */
@Entity
data class BookShelfTable(
    @Unique
    val comicId: String, val comicName: String, var coverUrl: String,
    var readChapterPosition: Int,
    var chapterSize: Int,
    var readTime : Long,
    @Id var id: Long = 0
)