package com.benyq.guochat.chat.model.net

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
 * @time 2020/6/2
 * @e-mail 1520063035@qq.com
 * @note
 */
@Module
@InstallIn(SingletonComponent::class)
object ChatServiceFactory {

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
    fun provideApiService(): ChatApiService {
        initClient()
        return RetrofitFactory.create(ChatApiService::class.java)
    }

}