package com.benyq.guochat.comic.model.http

import com.benyq.guochat.comic.model.http.fix_gson_converter.FixGsonConverterFactory
import com.benyq.module_base.ext.isJson
import com.benyq.module_base.ext.logi
import com.benyq.module_base.http.RetrofitFactory
import com.orhanobut.logger.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@Module
@InstallIn(SingletonComponent::class)
object ComicServiceFactory {
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
            addInterceptor(loggingInterceptor)
        }
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