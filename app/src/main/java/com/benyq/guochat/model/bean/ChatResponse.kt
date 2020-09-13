package com.benyq.guochat.model.bean

import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.http.ApiException
import com.benyq.mvvm.response.BenyqResponse

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ChatResponse<T>(val code: Int, val msg: String, val data: T?) : BenyqResponse<T> {

    companion object {
        fun <R> success(data: R?): ChatResponse<R> {
            return ChatResponse(0, "success", data)
        }

        fun <R> error(msg: String, errorCode: Int = -1): ChatResponse<R> {
            return ChatResponse(errorCode, msg, null)
        }
    }

    override fun isSuccess() = code == 0

    override fun getMessage() = msg

    override fun getRealData() = data

    override fun execute(success: ((T?) -> Unit)?, error: ((Exception) -> Unit)?) {
        if (!this.isSuccess()) {
            this.getMessage().let { error?.invoke(ApiException(code, msg)) ?: Toasts.show(it) }
            return
        }
        if (data == null) {
            this.getMessage().let { error?.invoke(ApiException(code, "数据不能是null")) ?: Toasts.show(it) }
            return
        }
        success?.invoke(this.getRealData()!!)
    }
}