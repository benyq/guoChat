package com.benyq.guochat.wanandroid.model.http

import com.benyq.guochat.wanandroid.WanAndroidInit
import com.benyq.module_base.ext.isJson
import com.benyq.module_base.ext.logi
import com.benyq.module_base.http.RetrofitFactory
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author benyq
 * @time 2021/8/21
 * @e-mail 1520063035@qq.com
 * @note
 */
@Module
@InstallIn(SingletonComponent::class)
object WanAndroidServiceFactory {

    // 读超时
    private const val READ_TIME_OUT = 20L
    // 写超时
    private const val WRITE_TIME_OUT = 30L
    // 连接超时
    private const val CONNECT_TIME_OUT = 5L

    private fun initClient() {
        val loggingInterceptor = HttpLoggingInterceptor { message ->
            if (message.isJson()) {
                Logger.json(message)
            } else {
                logi(message)
            }
        }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        RetrofitFactory.init {
            readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
            writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
            connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
            cookieJar(getCookieJar())
        }
    }

    @Singleton
    @Provides
    fun provideWanAndroidService(): WanAndroidApi {
        initClient()
        return RetrofitFactory.create(WanAndroidApi::class.java)
    }

    private fun getCookieJar(): ClearableCookieJar {
        return PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(WanAndroidInit.instance))
    }

}