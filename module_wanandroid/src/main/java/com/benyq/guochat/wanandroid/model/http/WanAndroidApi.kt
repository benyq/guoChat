package com.benyq.guochat.wanandroid.model.http

import com.benyq.guochat.wanandroid.model.LoginData
import com.benyq.guochat.wanandroid.model.PageData
import com.benyq.guochat.wanandroid.model.PersonScoreData
import com.benyq.guochat.wanandroid.model.WanResult
import com.benyq.guowanandroid.model.*
import com.benyq.module_base.annotation.BaseUrl
import retrofit2.http.*

@BaseUrl("https://www.wanandroid.com")
interface WanAndroidApi {

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(@Field("username")username: String, @Field("password")password: String): WanResult<LoginData>


    @GET("/lg/coin/userinfo/json")
    suspend fun personScore(): WanResult<PersonScoreData>

    @GET("/article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): WanResult<PageData<ArticleData>>

    @GET("/banner/json")
    suspend fun banner(): WanResult<List<BannerData>>
}