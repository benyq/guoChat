package com.benyq.guochat.function.music

import android.app.Service
import android.content.Intent
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.module_base.ext.loge

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note
 */
class PlayMusicService : Service() {

    companion object {
        const val ACTION_PLAY_TOGGLE = "com.benyq.guochat.ACTION.PLAY_TOGGLE"
        const val ACTION_PLAY_PLAY = "com.benyq.guochat.ACTION.PLAY_PLAY"
        const val ACTION_PLAY_PAUSE = "com.benyq.guochat.ACTION.PLAY_PAUSE"
        const val ACTION_PLAY_LAST = "com.benyq.guochat.ACTION.PLAY_LAST"
        const val ACTION_PLAY_NEXT = "com.benyq.guochat.ACTION.PLAY_NEXT"
        const val ACTION_STOP_SERVICE = "com.benyq.guochat.ACTION.STOP_SERVICE"
    }


    override fun onBind(intent: Intent?) = null

    override fun onCreate() {
        super.onCreate()
        loge("PlayMusicService onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotificationHelper.showMusicNotification(this, isPlaying())
        return super.onStartCommand(intent, flags, startId)
    }


    fun isPlaying() = PlayerController.isPlaying()
}