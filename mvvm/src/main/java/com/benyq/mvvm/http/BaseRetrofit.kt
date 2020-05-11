package com.benyq.mvvm.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * author：  HyZhan
 * create：  2019/7/24
 * desc：    TODO
 */
object BaseRetrofit {

    fun create(baseUrl: String): Retrofit {

        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(BaseOkHttpClient.create())
                .build()
    }
}