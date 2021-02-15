package com.benyq.guochat.comic.model.http

import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.http.BenyqResponse
import com.google.gson.annotations.SerializedName

/**
 * @author benyq
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ComicResponse<T>(
    val code: Int,
    @SerializedName("message")
    val msg: String = "", val data: DataBean<T>?
) : BenyqResponse<T> {

    override fun isSuccess() = code == 1 && data?.stateCode == 1

    override fun getMessage(): String = data?.message ?: msg

    override fun getRealData(): T? = data?.returnData

    override fun execute(success: ((T?) -> Unit)?, error: ((Exception) -> Unit)?) {
        if (!this.isSuccess()) {
            this.getMessage()
                .let { error?.invoke(ApiException(code, getMessage())) ?: Toasts.show(it) }
            return
        }
        if (getRealData() == null) {
            this.getMessage()
                .let { error?.invoke(ApiException(code, "数据不能是null")) ?: Toasts.show(it) }
            return
        }
        success?.invoke(this.getRealData()!!)
    }

    companion object {
        fun <R> success(data: R): ComicResponse<R> {
            return ComicResponse(1, "success", DataBean(1, "", data))
        }

        fun <R> error(msg: String, errorCode: Int = -1): ComicResponse<R> {
            return ComicResponse(errorCode, msg, null)
        }
    }
}


data class DataBean<T>(val stateCode: Int, val message: String, val returnData: T)