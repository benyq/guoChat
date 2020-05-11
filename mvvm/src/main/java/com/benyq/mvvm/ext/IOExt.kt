package com.benyq.mvvm.ext

import android.util.Log
import java.io.Closeable
import java.io.IOException

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/2/7
 * description:
 */
fun ioClose(closeable: Closeable?) {
    if (closeable == null) return
    try {
        closeable.close()
    } catch (e: IOException) {
        e.printStackTrace()
        //close error
    }

}

inline fun tryCatch(tryBlock: () -> Unit, catchBlock: (Throwable) -> Unit = {}, finalBlock: ()->Unit = {}) {
    try {
        tryBlock()
    } catch (t: Throwable) {
        t.printStackTrace()
        catchBlock(t)
        Log.e("benyq", "Throwable ${t.message}")
    }finally {
        finalBlock()
    }
}