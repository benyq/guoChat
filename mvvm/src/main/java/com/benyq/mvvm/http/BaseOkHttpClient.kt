package com.benyq.mvvm.http

import com.benyq.mvvm.Setting
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
object BaseOkHttpClient {

    private var hasInit = false
    private val builder = OkHttpClient.Builder()

    fun init(vararg interceptors: Interceptor) {
        if (hasInit){
            return
        }
        hasInit = !hasInit
        interceptors.forEach {
            builder.addInterceptor(it)
        }

        builder.readTimeout(Setting.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(Setting.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(Setting.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)


    }

    // 初始化 okHttp
    fun create() = builder.build()
}