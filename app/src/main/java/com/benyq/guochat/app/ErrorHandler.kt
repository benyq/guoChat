package com.benyq.guochat.app

import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge

/**
 * @author benyq
 * @time 2020/7/21
 * @e-mail 1520063035@qq.com
 * @note
 */
object ErrorHandler {

    fun handle(throwable: Throwable){
        loge(throwable)
        Toasts.show(throwable.message ?: "")
    }
}