package com.benyq.guochat.function.message

import android.os.Handler
import android.os.Looper
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

/**
 * @author benyq
 * @time 2020/6/3
 * @e-mail 1520063035@qq.com
 * @note websocket
 */
object WebSocketManager {

    private val url = "ws:111.229.84.254:7000/ws"
    private var mWebSocketClient: WebSocketClient ? = null
    private val wsReConnectRunnable = Runnable {
        mWebSocketClient?.reconnect()
    }

    private val mHandler = Handler(Looper.getMainLooper())

    private fun initClient(){
        val uri = URI(url)
        mWebSocketClient = mWebSocketClient ?: object: WebSocketClient(uri){

            override fun onOpen(handshakedata: ServerHandshake?) {
                //初始化关联user信息与设备信息
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                mHandler.removeCallbacks(wsReConnectRunnable)
                mHandler.postDelayed(wsReConnectRunnable, 5000)
            }

            override fun onMessage(message: String?) {
                //子线程
                message?.run {

                }
            }

            override fun onError(ex: Exception?) {
                mHandler.removeCallbacks(wsReConnectRunnable)
                mHandler.postDelayed(wsReConnectRunnable, 5000)
            }

        }
    }

    fun connectWs(){
        mHandler.removeCallbacks(wsReConnectRunnable)
        mWebSocketClient?.close()
        initClient()
        mWebSocketClient?.connect()
    }

    fun reconnect(){
        mHandler.removeCallbacks(wsReConnectRunnable)
        mWebSocketClient?.reconnect()
    }

    fun close(){
        mHandler.removeCallbacks(wsReConnectRunnable)
        mWebSocketClient?.close()
    }
}