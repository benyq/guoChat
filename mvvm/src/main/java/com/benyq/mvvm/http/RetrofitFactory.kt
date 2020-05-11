package com.benyq.mvvm.http

import com.benyq.mvvm.annotation.BaseUrl
import java.lang.IllegalArgumentException

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
object RetrofitFactory {

    fun <T> create(clz: Class<T>): T {

        val baseUrl = prepareBaseUrl(clz)

        val retrofit = BaseRetrofit.create(baseUrl)

        return retrofit.create(clz)
    }

    private fun <T> prepareBaseUrl(clz: Class<T>) : String{
        val baseUrlAnnotation = clz.getAnnotation(BaseUrl::class.java)
        return baseUrlAnnotation?.value ?: throw IllegalArgumentException("base url is null")
    }
}