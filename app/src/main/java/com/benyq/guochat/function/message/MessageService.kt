package com.benyq.guochat.function.message

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author benyq
 * @time 2020/6/3
 * @e-mail 1520063035@qq.com
 * @note  消息接收服务
 */
class MessageService : Service(){

    private var doConnect = false

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        WebSocketManager.connectWs()
        doConnect = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //防止第一次启动Service时重复连接
        if (!doConnect){
            WebSocketManager.reconnect()
        }
        doConnect = false
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }
}