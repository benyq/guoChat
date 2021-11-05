package com.benyq.guochat.wanandroid.model.http

import androidx.lifecycle.LiveData
import com.benyq.guochat.wanandroid.model.*
import com.benyq.module_base.annotation.BaseUrl
import retrofit2.http.*

@BaseUrl("https://www.wanandroid.com")
interface WanAndroidApi {

    @POST("/user/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): WanResult<LoginData>


    @GET("/lg/coin/userinfo/json")
    suspend fun personScore(): WanResult<PersonScoreData>

    @GET("/article/list/{page}/json")
    suspend fun articleList(@Path("page") page: Int): WanResult<PageData<ArticleData>>

    @GET("/banner/json")
    suspend fun banner(): WanResult<List<BannerData>>

    @GET("project/tree/json")
    suspend fun listProjectsTab(): WanResult<List<ProjectTreeData>>

    @GET("project/list/{page}/json")
    suspend fun listProjects(
        @Path("page") page: Int,
        @Query("cid") id: String
    ): WanResult<PageData<ArticleData>>


    /**
     * 公众号文章列表
     *
     * @param id   公众号id
     * @param page 页码，拼接在连接中，从0开始。
     * @return
     */
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun listWechatArticle(
        @Path("id") id: String?,
        @Path("page") page: Int
    ): WanResult<PageData<ArticleData>>


    /**
     * 公众号作者列表
     *
     * @return
     */
    @GET("wxarticle/chapters/json")
    suspend fun listWechatAuthor(): WanResult<List<WechatAuthorData>>

}