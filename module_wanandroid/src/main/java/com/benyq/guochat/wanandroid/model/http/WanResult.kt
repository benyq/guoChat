package com.benyq.guochat.wanandroid.model

import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.http.BenyqResponse
import com.google.gson.annotations.SerializedName

/**
 * @author benyq
 * @date 2021/8/5
 * @email 1520063035@qq.com
 */
data class WanResult<T>(val errorCode: Int, val errorMsg: String, val data: T?): BenyqResponse<T> {
    companion object {
        fun error(msg: String): WanResult<Any> {
            return WanResult(-1, msg, Any())
        }
    }

    override fun isSuccess() = errorCode == 0 && data != null

    override fun getMessage() = errorMsg

    override fun getRealData(): T? = data

    override fun execute(success: ((T?) -> Unit)?, error: ((Exception) -> Unit)?) {
        if (!this.isSuccess()) {
            this.getMessage()
                .let { error?.invoke(ApiException(errorCode, getMessage())) ?: Toasts.show(it) }
            return
        }
        if (getRealData() == null) {
            this.getMessage()
                .let { error?.invoke(ApiException(errorCode, "数据不能是null")) ?: Toasts.show(it) }
            return
        }
        success?.invoke(this.getRealData()!!)
    }
}


data class PageData<T>(
    val curPage: Int,
    @SerializedName("datas")
    val data: List<T>,
    val offset: Int,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val over: Boolean,
)