package com.benyq.guochat.chat.function.message

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.chat.app.SharedViewModel
import com.benyq.guochat.chat.app.baseUrl
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.bean.ChatMessageBean
import com.benyq.guochat.database.entity.chat.ChatRecordEntity
import com.benyq.module_base.ext.logd
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ext.tryCatch
import com.benyq.module_base.ui.base.BaseApplication
import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * @author benyq
 * @time 2020/6/3
 * @e-mail 1520063035@qq.com
 * @note websocket
 */
@SuppressLint("StaticFieldLeak")
object WebSocketManager {

    private val url = "ws://81.69.26.237:8080/socket"
    private val gson = Gson()
    private lateinit var context: Context

    private lateinit var appViewModelProvider: ViewModelProvider
    private lateinit var viewModel: SharedViewModel

    private var mWebSocketClient: WebSocketClient? = null
    private val wsReConnectRunnable = Runnable {
        mWebSocketClient?.reconnect()
    }
    private val heartRunnable: Runnable = object : Runnable {
        override fun run() {
            sendMessage(gson.toJson(WSMessage(WSMessage.TYPE_HEART, "")))
            mHandler.postDelayed(this, 10000)
        }
    }

    private val mHandler = Handler(Looper.getMainLooper())

    private fun initClient(context: Context) {
        this.context = context.applicationContext
        appViewModelProvider = ViewModelProvider(
            this.context as BaseApplication,
            ViewModelProvider.AndroidViewModelFactory.getInstance(
                this.context as BaseApplication
            )
        )
        viewModel = appViewModelProvider.get(SharedViewModel::class.java)
        val uri = URI(url)
        mWebSocketClient = mWebSocketClient ?: object : WebSocketClient(uri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                logd("ws onOpen")
                mHandler.removeCallbacks(heartRunnable)
                mHandler.postDelayed(heartRunnable, 10000)
                //初始化关联user信息与设备信息
                sendMessage(
                    gson.toJson(
                        WSMessage(
                            WSMessage.TYPE_UID,
                            "chat-${ChatLocalStorage.uid}"
                        )
                    )
                )
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                logd("ws onClose:  $reason")

                mHandler.removeCallbacks(wsReConnectRunnable)
                mHandler.postDelayed(wsReConnectRunnable, 5000)
            }

            override fun onMessage(message: String?) {
                logd("ws onMessage:  $message")
                try {
                    val messageBean: ChatMessageBean =
                        gson.fromJson(message, ChatMessageBean::class.java)
                    dealChatMessage(messageBean)
                } catch (e: Exception) {
                }

                //子线程
                message?.run {

                }
            }

            override fun onError(ex: Exception?) {
                logd("ws onError:  ${ex?.message}")
                mHandler.removeCallbacks(wsReConnectRunnable)
                mHandler.postDelayed(wsReConnectRunnable, 5000)
            }

        }
    }

    fun connectWs(context: Context) {
        mHandler.removeCallbacks(wsReConnectRunnable)
        mWebSocketClient?.close()
        initClient(context)
        mWebSocketClient?.connect()
    }

    fun reconnect() {
        mHandler.removeCallbacks(wsReConnectRunnable)
        mHandler.removeCallbacks(heartRunnable)
        mWebSocketClient?.reconnect()
    }

    fun close() {
        mHandler.removeCallbacks(wsReConnectRunnable)
        mHandler.removeCallbacks(heartRunnable)
        mWebSocketClient?.close()
        mWebSocketClient = null
    }

    private fun sendMessage(msg: String) {
        tryCatch({
            mWebSocketClient?.send(msg)
        }, {
            loge("ws send message: ${it.message}")
        }, {

        })
    }

    private fun dealChatMessage(bean: ChatMessageBean) {
        val conversation = ChatObjectBox.getConversationId(bean.toId, bean.fromId)
        val contract = ChatObjectBox.getContractByConversation(conversation.id)
        //先保存本地数据库
        val chatRecord = ChatRecordEntity(
            fromUid = bean.fromId,
            toUid = bean.toId,
            conversationId = conversation.id,
            sendTime = bean.sendTime,
            chatType = bean.type
        )
        if (bean.type == ChatRecordEntity.TYPE_TEXT) {
            chatRecord.content = bean.msg
        } else {
            chatRecord.filePath = baseUrl + bean.msg
        }
        ChatObjectBox.saveChatMessage(chatRecord)
        ChatObjectBox.updateChatRecord(conversation.id, bean.sendTime)
        val lastConversion: String = chatRecord.let { record ->
            when (record.chatType) {
                ChatRecordEntity.TYPE_FILE -> {
                    "[文件]"
                }
                ChatRecordEntity.TYPE_VOICE -> {
                    "[语音]"
                }
                ChatRecordEntity.TYPE_VIDEO -> {
                    "[视频]"
                }
                ChatRecordEntity.TYPE_IMG -> {
                    "[图片]"
                }
                else -> {
                    record.content
                }
            }
        }
        viewModel.notifyChatChange(conversation.id, contract!!, lastConversion)
        //发送通知，ChatFragment 以及 notification
    }
}