package com.benyq.guochat.model.bean

import com.benyq.mvvm.Setting
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.response.BenyqResponse

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ChatResponse<T>(val code: Int, val msg: String, val data: T?) : BenyqResponse<T> {

    companion object {
        fun <R> success(data: R?) : ChatResponse<R>{
            return ChatResponse(0, "success", data)
        }
        fun <R> error(msg: String) : ChatResponse<R>{
            return ChatResponse(-1, msg, null)
        }
    }

    override fun isSuccess() = code == 0

    override fun getMessage() = msg

    override fun getRealData() = data

    override fun execute(success: ((T?) -> Unit)?, error: ((String) -> Unit)?) {
        if (this.isSuccess() && data != null) {
            success?.invoke(this.getRealData()!!)
            return
        }
        throw Throwable("数据不能是null")
//        this.getMessage().let { error?.invoke(it) ?: Toasts.show(it) }
    }
}