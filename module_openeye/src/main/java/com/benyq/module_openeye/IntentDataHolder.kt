package com.benyq.module_openeye

import java.lang.ref.WeakReference

/**
 * @author benyq
 * @time 2020/9/12
 * @e-mail 1520063035@qq.com
 * @note
 *  数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现的异常场景。
 */
object IntentDataHolder {

    private val dataList = hashMapOf<String, WeakReference<Any>>()

    fun setData(key: String, t: Any) {
        val value = WeakReference(t)
        dataList[key] = value
    }

    fun <T> getData(key: String): T? {
        val reference = dataList[key]
        return try {
            reference?.get() as T
        } catch (e: Exception) {
            null
        }
    }

}