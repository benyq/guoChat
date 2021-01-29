package com.benyq.mvvm.ext

import com.benyq.mvvm.BuildConfig
import com.socks.library.KLog

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */

object Log {

    fun init() {
        KLog.init(BuildConfig.DEBUG, "benyq")
    }
}

fun loge(obj: Any){
    KLog.e("benyq", obj)
}

fun logi(obj: Any){
    KLog.i("benyq", obj)
}

fun logd(obj: Any){
    KLog.i("benyq", obj)
}

fun logw(obj: Any){
    KLog.w("benyq", obj)
}

fun String.showLog() {
    logd("<<<<<<--------------------------")
    logd("[content]:  $this")
    logd("-------------------------->>>>>>")
}