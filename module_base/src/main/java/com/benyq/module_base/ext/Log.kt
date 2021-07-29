package com.benyq.module_base.ext

import com.orhanobut.logger.Logger


/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */

fun loge(obj: Any){
    Logger.e("benyq", obj)
}

fun logi(obj: Any){
    Logger.i("benyq", obj)
}

fun logd(obj: Any){
    Logger.i("benyq", obj)
}

fun logw(obj: Any){
    Logger.w("benyq", obj)
}

fun String.showLog() {
    logd("<<<<<<--------------------------")
    logd("[content]:  $this")
    logd("-------------------------->>>>>>")
}