package com.benyq.guochat.comic.model.http

import com.benyq.guochat.comic.model.http.fix_gson_converter.FixGsonConverterFactory
import com.benyq.module_base.ext.isJson
import com.benyq.module_base.ext.logi
import com.benyq.module_base.http.RetrofitFactory
import com.socks.library.KLog
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@Module
@InstallIn(ApplicationComponent::class)
object ComicServiceFactory {
    private fun initClient() {
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

        RetrofitFactory.init(loggingInterceptor)
    }

    @Singleton
    @Provides
    fun provideApiService(): ComicApiService {
        initClient()
        return RetrofitFactory.create(ComicApiService::class.java) {
            addConverterFactory(FixGsonConverterFactory.create())
        }
    }
}