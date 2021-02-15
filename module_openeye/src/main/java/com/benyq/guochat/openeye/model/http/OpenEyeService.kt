package com.benyq.guochat.openeye.model.http

import com.benyq.module_base.annotation.BaseUrl
import com.benyq.guochat.openeye.bean.CommunityRecommend
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note 开眼app 接口
 */
@BaseUrl("http://baobab.kaiyanapp.com/")
interface OpenEyeService {


    companion object{

        const val BASE_URL = "http://baobab.kaiyanapp.com/"
        /**
         * 社区-推荐列表
         */
        const val COMMUNITY_RECOMMEND_URL = "${BASE_URL}api/v7/community/tab/rec"
    }

    /**
     * 社区-推荐列表
     */
    @GET
    suspend fun getCommunityRecommend(@Url url: String): CommunityRecommend

    /**
     * 搜索-热搜关键词
     */
    @GET("api/v3/queries/hot")
    suspend fun getHotSearch(): List<String>
}