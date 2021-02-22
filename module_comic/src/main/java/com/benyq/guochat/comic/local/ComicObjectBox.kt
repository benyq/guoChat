package com.benyq.guochat.comic.local

import com.benyq.guochat.database.DataObjectBox
import com.benyq.guochat.database.entity.comic.BookShelfTable
import com.benyq.guochat.database.entity.comic.SearchHistoryRecord
import io.objectbox.Box

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
object ComicObjectBox {

    const val searchHistoryLimit = 10L

    val searchHistoryBox: Box<SearchHistoryRecord> by lazy { DataObjectBox.searchHistoryBox }
    val bookShelfBox: Box<BookShelfTable> by lazy { DataObjectBox.bookShelfBox }
}