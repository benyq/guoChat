package com.benyq.guochat.openeye.model.http

import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.http.BenyqResponse

/**
 * @author benyq
 * @time 2021/2/13
 * @e-mail 1520063035@qq.com
 * @note
 */

data class OpenEyeResponse<T>(val code: Int, val msg: String, val data: T?) : BenyqResponse<T> {

    companion object {
        fun <R> success(data: R?): OpenEyeResponse<R> {
            return OpenEyeResponse(0, "success", data)
        }

        fun <R> error(msg: String, errorCode: Int = -1): OpenEyeResponse<R> {
            return OpenEyeResponse(errorCode, msg, null)
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