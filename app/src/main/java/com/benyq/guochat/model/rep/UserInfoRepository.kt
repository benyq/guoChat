package com.benyq.guochat.model.rep

import com.benyq.guochat.app.STREAM
import com.benyq.guochat.app.TEXT
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.guochat.model.net.ServiceFactory
import com.benyq.mvvm.mvvm.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

/**
 * @author benyq
 * @time 2020/6/4
 * @e-mail 1520063035@qq.com
 * @note  用户信息的数据源
 */
class UserInfoRepository : BaseRepository(){

    suspend fun uploadAvatar(file: File): ChatResponse<String> {
        return launchIO {
            val requestFile = file.asRequestBody(STREAM)
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val userBody = LocalStorage.uid.toRequestBody(TEXT)
            val map = mutableMapOf<String, RequestBody>()
            map["uid"] = userBody

            ServiceFactory.apiService.uploadAvatar(map, body)
        }
    }

    suspend fun editUserNick(nickName: String): ChatResponse<String>{
        return launchIO {
            ServiceFactory.apiService.editUserNick(LocalStorage.uid, nickName)
        }
    }

}