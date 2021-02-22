package com.benyq.guochat.database

import android.content.Context
import android.util.Log
import com.benyq.guochat.database.entity.comic.BookShelfTable
import com.benyq.guochat.database.entity.MyObjectBox
import com.benyq.guochat.database.entity.comic.SearchHistoryRecord
import io.objectbox.Box
import io.objectbox.BoxStore
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.kotlin.boxFor

/**
 * @author benyqYe
 * date 2021/2/22
 * e-mail 1520063035@qq.com
 * description 所有模块的本地数据库操作
 * ObjectBrowser: ObjectBrowser started: http://localhost:8090/index.html
 * ObjectBrowser: Command to forward ObjectBrowser to connected host: adb forward tcp:8090 tcp:8090
 */

object DataObjectBox {

    lateinit var boxStore: BoxStore


    val searchHistoryBox: Box<SearchHistoryRecord> by lazy { boxStore.boxFor() }
    val bookShelfBox: Box<BookShelfTable> by lazy { boxStore.boxFor() }



    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context.applicationContext).build()

        if (BuildConfig.DEBUG) {
            val started = AndroidObjectBrowser(boxStore).start(context)
            Log.i("DataObjectBox", "init: ObjectBrowser Started: $started")
        }

    }
}