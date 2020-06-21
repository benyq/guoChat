package com.benyq.guochat.model.net

import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.bean.UserBean
import com.benyq.mvvm.annotation.BaseUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author benyq
 * @time 2020/6/2
 * @e-mail 1520063035@qq.com
 * @note
 */
@BaseUrl("")
interface ApiService {

    /**
     * 登录
     */
    @POST
    @FormUrlEncoded
    suspend fun login(@Field("account") account: String, @Field("password") password: String): ChatResponse<UserBean>


    /**
     * 上传头像
     */
    @POST("")
    @Multipart
    suspend fun uploadAvatar(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part): ChatResponse<String>

    /**
     * 修改自己的昵称
     */
    @POST("")
    @FormUrlEncoded
    suspend fun editUserNick(@Field("uid") uid: String, @Field("nick") nick: String): ChatResponse<String>
}