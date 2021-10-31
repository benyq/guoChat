package com.benyq.guochat.chat.model.net

import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.bean.ContractBean
import com.benyq.guochat.chat.model.bean.UserBean
import com.benyq.module_base.annotation.BaseUrl
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @author benyq
 * @time 2020/6/2
 * @e-mail 1520063035@qq.com
 * @note
 */
@BaseUrl("http://81.69.26.237:8080/")
interface ChatApiService {

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(@Field("phone") account: String, @Field("pwd") password: String): ChatResponse<UserBean>

    @POST("user/register")
    suspend fun register(@Body body: RequestBody): ChatResponse<UserBean>

    /**
     * 上传头像
     */
    @POST("user/upload-avatar")
    @Multipart
    suspend fun uploadAvatar(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part): ChatResponse<String>

    /**
     * 修改自己的昵称
     */
    @POST("user/edit-nick")
    @FormUrlEncoded
    suspend fun editUserNick(@Field("nick") nick: String): ChatResponse<String>

    @GET("/")
    suspend fun test(): String

    /**
     * 搜索联系人
     */
    @GET("/contract/search-contract")
    suspend fun searchContract(@Query("key") key: String): ChatResponse<ContractBean>

    /**
     * 获取所有联系人
     */
    @GET("/contract/get-all-contract")
    suspend fun getAllContracts(): ChatResponse<List<ContractBean>>

    /**
     * @param uid 联系人id
     */
    @POST("/contract/apply-contract")
    @FormUrlEncoded
    suspend fun applyContract(@Field("apply_id") uid: String): ChatResponse<String>

    /**
     * @param contractId 联系人申请id
     */
    @POST("/contract/agree-contract")
    @FormUrlEncoded
    suspend fun agreeContract(@Field("contract_id") contractId: String): ChatResponse<String>

    @POST("/contract/refuse-contract")
    @FormUrlEncoded
    suspend fun refuseContract(@Field("contract_id") contractId: String): ChatResponse<String>


    @POST("/chat/send-message")
    @FormUrlEncoded
    suspend fun sendChatMessage(@Field("to") to: String, @Field("msg") msg: String): ChatResponse<Boolean>

    @POST("/chat/send-chat-file")
    @Multipart
    suspend fun sendChatFile(@PartMap map: Map<String, @JvmSuppressWildcards RequestBody>, @Part file: MultipartBody.Part): ChatResponse<Boolean>

}