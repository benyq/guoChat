package com.benyq.guochat.model.net

import com.benyq.mvvm.ext.isJson
import com.benyq.mvvm.ext.logi
import com.benyq.mvvm.http.BaseOkHttpClient
import com.benyq.mvvm.http.RetrofitFactory
import com.socks.library.KLog
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author benyq
 * @time 2020/6/2
 * @e-mail 1520063035@qq.com
 * @note
 */
object ServiceFactory {

    fun initClient() {
        val loggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                if (message.isJson()){
                    KLog.json(message)
                }else {
                    logi(message)
                }
            }
        })
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        BaseOkHttpClient.init(ParamsInterceptor, loggingInterceptor)
    }

    val apiService by lazy { RetrofitFactory.create(ApiService::class.java) }

}