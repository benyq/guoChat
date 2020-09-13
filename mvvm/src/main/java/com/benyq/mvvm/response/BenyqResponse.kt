package com.benyq.mvvm.response

import com.benyq.mvvm.Setting
import com.benyq.mvvm.ext.Toasts

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
interface BenyqResponse<T> {

    fun isSuccess(): Boolean

    fun getMessage(): String?

    fun getRealData(): T?

    /**
     *  全局默认实现, 可根据自身业务 重写execute方法
     *  @param error            若有错误的回调, 默认getMessage(), 否则返回 Setting.MESSAGE_EMPTY
     *  @param successResponse  成功的回调, 默认是返回 BenyqResponse<T>
     */
    fun executeRsp(successResponse: ((BenyqResponse<T>) -> Unit)?, error: ((Exception) -> Unit)? = null) {

        if (this.isSuccess()) {
            successResponse?.invoke(this)
            return
        }

        (this.getMessage() ?: Setting.MESSAGE_EMPTY).let { error?.invoke(Exception(it)) ?: Toasts.show(it)}
    }

    /**
     *  全局默认实现, 可根据自身业务 重写execute方法
     *  @param success          成功的回调, 默认是返回 getData()
     *  @param error            若有错误的回调, 默认getMessage(), 否则返回 Setting.MESSAGE_EMPTY
     */
    fun execute(success: ((T?) -> Unit)?, error: ((Exception) -> Unit)? = null) {

        if (this.isSuccess()) {
            success?.invoke(this.getRealData())
            return
        }

        (this.getMessage() ?: Setting.MESSAGE_EMPTY).let { error?.invoke(Exception(it)) ?: Toasts.show(it) }
    }

}