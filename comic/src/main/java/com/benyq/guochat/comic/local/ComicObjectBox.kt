package com.benyq.guochat.comic.local

import android.content.Context
import com.benyq.mvvm.ext.logi
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.android.BuildConfig
import io.objectbox.kotlin.boxFor

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
object ComicObjectBox {

    const val searchHistoryLimit = 10L

    private lateinit var boxStore: BoxStore

    val searchHistoryBox: Box<SearchHistoryRecord> by lazy { boxStore.boxFor() }
    val bookShelfBox: Box<BookShelfTable> by lazy { boxStore.boxFor() }

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .name("comic")
            .androidContext(context.applicationContext).build()

    }

}