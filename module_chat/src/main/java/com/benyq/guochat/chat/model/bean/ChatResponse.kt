package com.benyq.guochat.chat.model.bean

import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.loge
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.http.BenyqResponse
import com.benyq.module_base.http.HttpSetting
import com.google.gson.annotations.SerializedName

/**
 * @author benyq
 * @time 2020/5/2
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ChatResponse<T>(
    @SerializedName("errorCode")
    val code: Int,
    @SerializedName("errorMsg")
    val msg: String?, val data: T?) : BenyqResponse<T> {

    companion object {
        fun <R> success(data: R?): ChatResponse<R> {
            return ChatResponse(0, "success", data)
        }

        fun <R> error(msg: String, errorCode: Int = -1): ChatResponse<R> {
            return ChatResponse(errorCode, msg, null)
        }
    }

    override fun isSuccess() = code == 0

    override fun getMessage() = msg ?: ""

    override fun getRealData() = data

    override fun execute(success: ((T?) -> Unit)?, error: ((Exception) -> Unit)?) {
        if (!this.isSuccess()) {
            this.getMessage().let { error?.invoke(ApiException(code, it)) ?: Toasts.show(it) }
            return
        }
        if (data == null) {
            this.getMessage().let { error?.invoke(ApiException(code, "数据不能是null")) ?: Toasts.show(it) }
            return
        }
        success?.invoke(this.getRealData()!!)
    }

    override fun executeRsp(successResponse: ((BenyqResponse<T>) -> Unit)?, error: ((Exception) -> Unit)?) {
        successResponse?.invoke(ChatResponse(code, getMessage(), data))
        if (!this.isSuccess()) {
            this.getMessage().let { error?.invoke(ApiException(code, it)) ?: Toasts.show(it) }
            return
        }
    }
}