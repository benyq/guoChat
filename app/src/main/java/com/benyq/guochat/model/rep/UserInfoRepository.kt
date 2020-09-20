package com.benyq.guochat.model.rep

import com.benyq.guochat.local.ChatLocalStorage
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.net.ApiService
import com.benyq.mvvm.STREAM
import com.benyq.mvvm.TEXT
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/6/4
 * @e-mail 1520063035@qq.com
 * @note  用户信息的数据源
 */
class UserInfoRepository @Inject constructor(private val apiService: ApiService) : BaseRepository(){

    suspend fun uploadAvatar(file: File): ChatResponse<String> {
        return launchIO {
            val requestFile = file.asRequestBody(STREAM)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val userBody = ChatLocalStorage.uid.toRequestBody(TEXT)
            val map = mutableMapOf<String, RequestBody>()
            map["uid"] = userBody
            loge("UserInfoRepository apiService $apiService")
            apiService.uploadAvatar(map, body)
        }
    }

    suspend fun editUserNick(nickName: String): ChatResponse<String>{
        return launchIO {
            loge("UserInfoRepository apiService $apiService")
            apiService.editUserNick(ChatLocalStorage.uid, nickName)
        }
    }

}