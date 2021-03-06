package com.benyq.module_base.ext

import com.benyq.module_base.BuildConfig
import com.socks.library.KLog

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */

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