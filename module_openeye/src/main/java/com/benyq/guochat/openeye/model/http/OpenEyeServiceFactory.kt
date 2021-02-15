package com.benyq.guochat.openeye.model.http

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
 * @time 2021/2/13
 * @e-mail 1520063035@qq.com
 * @note
 */
@Module
@InstallIn(ApplicationComponent::class)
object OpenEyeServiceFactory {

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
    fun provideOpenEyeService(): OpenEyeService {
        initClient()
        return RetrofitFactory.create(OpenEyeService::class.java)
    }
}