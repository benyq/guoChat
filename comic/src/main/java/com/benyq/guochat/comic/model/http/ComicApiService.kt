package com.benyq.guochat.comic.model.http

import com.benyq.guochat.comic.model.bean.*
import com.benyq.module_base.annotation.BaseUrl
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note 漫画api
 */
@BaseUrl("http://app.u17.com/v3/appV3_3/android/phone/")
interface ComicApiService {

    /**
     * 首页内容
     */
    @GET("comic/boutiqueListNew")
    suspend fun boutiqueList(@Query("sexType") sexType: Int): ComicResponse<RecommendEntity>

    /**
     * 搜索热点
     */
    @GET("search/hotkeywordsnew")
    suspend fun comicSearchHot(): ComicResponse<SearchHotEntity>

    /**
     * 搜索结果
     */
    @GET("search/searchResult")
    suspend fun comicSearch(@Query("q") text: String, @Query("page") page: Int):ComicResponse<ComicSearchResponse>


    @GET("comic/detail_static_new")
    suspend fun comicDetail(@Query("comicid") comicId: String): ComicResponse<ComicDetailResponse>

    /**
     * 动漫章节内容
     */
    @GET("comic/chapterNew")
    suspend fun comicPreView(@Query("chapter_id") chapter_id: String): ComicResponse<ComicPreViewResponse>
}
