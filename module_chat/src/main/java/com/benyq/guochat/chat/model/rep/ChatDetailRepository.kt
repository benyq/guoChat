package com.benyq.guochat.chat.model.rep

import android.text.TextUtils
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.guochat.database.entity.chat.ChatRecordEntity
import com.benyq.module_base.http.STREAM
import com.benyq.module_base.http.TEXT
import com.benyq.module_base.mvvm.BaseRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.w3c.dom.Text
import java.io.File
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatDetailRepository @Inject constructor(private val apiService: ChatApiService) : BaseRepository(){

    suspend fun getChatRecord(chatId: Long, page: Int, size: Int): ChatResponse<List<ChatRecordEntity>> {
        return launchIO {
            ChatResponse.success(ChatObjectBox.getChatRecord(chatId, page.toLong(), size.toLong()))
        }
    }

    suspend fun sendChatMessage(data: ChatRecordEntity) : ChatResponse<Boolean> {
        val response: ChatResponse<Boolean>
        if (data.chatType == ChatRecordEntity.TYPE_TEXT) {
            response = apiService.sendChatMessage(data.toUid, data.content)
        }else {
            var fileName = ""
            var typeRequestBody: RequestBody = "1".toRequestBody(TEXT)
            val file: File? = when(data.chatType) {
                ChatRecordEntity.TYPE_IMG -> {
                    fileName = "img"
                    typeRequestBody = "3".toRequestBody(TEXT)
                    File(data.filePath)
                }
                ChatRecordEntity.TYPE_VIDEO -> {
                    fileName = "video"
                    typeRequestBody = "4".toRequestBody(TEXT)
                    File(data.filePath)
                }
                ChatRecordEntity.TYPE_VOICE -> {
                    fileName = "voice"
                    typeRequestBody = "5".toRequestBody(TEXT)
                    File(data.filePath)
                }
                ChatRecordEntity.TYPE_FILE -> {
                    fileName = "file"
                    typeRequestBody = "2".toRequestBody(TEXT)
                    File(data.filePath)
                }
                else -> null
            }
            response = file?.let {
                val requestFile = file.asRequestBody(STREAM)
                val body = MultipartBody.Part.createFormData(fileName, file.name, requestFile)
                val map = mutableMapOf<String, RequestBody>()
                map["to"] = data.toUid.toRequestBody(TEXT)
                map["type"] = typeRequestBody
                apiService.sendChatFile(map, body)
            } ?: ChatResponse.error("上传文件不存在")
        }
        return launchIO {
            if (response.isSuccess()) {
                ChatObjectBox.updateChatRecord(data.conversationId)
                ChatObjectBox.saveChatMessage(data)
            }
            response
        }
    }
}