package com.benyq.module_base.http

import com.benyq.module_base.http.HttpSetting
import com.benyq.module_base.annotation.BaseUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
object RetrofitFactory {


    fun <T> create(clz: Class<T>): T {

        val baseUrl = prepareBaseUrl(clz)

        val retrofit = createRetrofit(baseUrl)

        return retrofit.create(clz)
    }

    private fun <T> prepareBaseUrl(clz: Class<T>) : String{
        val baseUrlAnnotation = clz.getAnnotation(BaseUrl::class.java)
        return baseUrlAnnotation?.value ?: throw IllegalArgumentException("base url is null")
    }

    private fun createRetrofit(baseUrl: String): Retrofit {

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttp())
            .build()
    }


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

        builder.readTimeout(HttpSetting.READ_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(HttpSetting.WRITE_TIME_OUT, TimeUnit.SECONDS)
            .connectTimeout(HttpSetting.CONNECT_TIME_OUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)


    }

    // 初始化 okHttp
    private fun createOkHttp(): OkHttpClient {
        return builder.build()
    }
}