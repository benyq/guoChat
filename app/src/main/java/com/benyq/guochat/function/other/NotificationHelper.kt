package com.benyq.guochat.function.other

import android.app.*
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.benyq.guochat.R
import com.benyq.guochat.function.music.PlayMusicService
import com.benyq.guochat.ui.common.NotificationHandleActivity
import com.benyq.guochat.ui.function.MusicPlayingActivity
import com.benyq.mvvm.ext.fromO

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note 通知栏管理
 */
object NotificationHelper {

    private const val MESSAGE_CHANNEL_ID = "channelIdMessage"
    private const val MESSAGE_CHANNEL_NAME = "普通消息"

    private const val OTHER_CHANNEL_ID = "channelIdOther"
    private const val OTHER_CHANNEL_NAME = "其他通知"

    private var messageNotify = 33
    private var musicNotify = 11

    private var progressNotify = 22

    private val mContentViewBig: RemoteViews? = null
    private var mContentViewSmall: RemoteViews? = null

    private var mProgressView: RemoteViews? = null

    lateinit var mNotificationManager : NotificationManager

    fun init(context: Context) {
        mNotificationManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    /**
     * 消息通知
     */
    fun showMessageNotification(context: Context) {
        messageNotify++
        val contentIntent = PendingIntent.getActivity(context, messageNotify, Intent(context, NotificationHandleActivity::class.java).apply {
            putExtra("ids", messageNotify)
        }, PendingIntent.FLAG_CANCEL_CURRENT)
        if (fromO()) {
            val notificationChannel =
                NotificationChannel(
                    MESSAGE_CHANNEL_ID,
                    MESSAGE_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        val notification: Notification = NotificationCompat.Builder(context, MESSAGE_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setContentTitle("苏打先生")
            .setContentText("我就说你这个事情是不可能完成的，你还不相信。相信我说的就是没问题的。不要你觉得，我要我觉得")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(contentIntent)
            .build()
        mNotificationManager.notify(messageNotify, notification)
    }

    fun showMusicNotification(context: Context, isPlaying: Boolean) {
        val contentIntent = PendingIntent.getActivity(context, musicNotify, Intent(context, MusicPlayingActivity::class.java), PendingIntent.FLAG_CANCEL_CURRENT)
        if (fromO()) {
            val notificationChannel =
                NotificationChannel(
                    OTHER_CHANNEL_ID,
                    OTHER_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
        val notification: Notification = NotificationCompat.Builder(context, OTHER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setCustomContentView(getMessageView(context, isPlaying))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(contentIntent)
            .build()
        if (context is Service) {
            context.startForeground(musicNotify, notification)
        }else {
            mNotificationManager.notify(musicNotify, notification)
        }
    }

    fun cancelMusicNotification() {
        mNotificationManager.cancel(musicNotify)
    }


    private fun getMessageView(context: Context, isPlaying: Boolean) : RemoteViews{
        mContentViewSmall = mContentViewSmall ?: RemoteViews(
            context.packageName,
            R.layout.remote_view_player_small
        ).apply {
            setUpRemoteView(this, context)
        }
        updateRemoteView(mContentViewSmall!!, isPlaying)
        return mContentViewSmall!!
    }

    private fun updateRemoteView(remoteView: RemoteViews, isPlaying: Boolean) {
        remoteView.setImageViewResource(
            R.id.image_view_play_toggle,
            if (isPlaying) R.drawable.ic_remote_view_play else R.drawable.ic_remote_view_pause
        )
    }

    private fun setUpRemoteView(remoteView: RemoteViews, context: Context) {
        remoteView.setImageViewResource(R.id.image_view_close, R.drawable.ic_remote_view_close)
        remoteView.setImageViewResource(
            R.id.image_view_play_last,
            R.drawable.ic_remote_view_play_last
        )
        remoteView.setImageViewResource(
            R.id.image_view_play_next,
            R.drawable.ic_remote_view_play_next
        )

        remoteView.setOnClickPendingIntent(
            R.id.button_play_last,
            getPendingIntent(PlayMusicService.ACTION_PLAY_LAST, context)
        )
        remoteView.setOnClickPendingIntent(
            R.id.button_play_next,
            getPendingIntent(PlayMusicService.ACTION_PLAY_NEXT, context)
        )
        remoteView.setOnClickPendingIntent(
            R.id.button_play_toggle,
            getPendingIntent(PlayMusicService.ACTION_PLAY_TOGGLE, context)
        )
        remoteView.setOnClickPendingIntent(R.id.button_close, getPendingIntent(PlayMusicService.ACTION_STOP_SERVICE, context))
    }

    private fun getPendingIntent(action: String, context: Context): PendingIntent {
        return PendingIntent.getBroadcast(context, 0, Intent(action).setPackage(context.packageName), PendingIntent.FLAG_UPDATE_CURRENT)
    }


    //测试下载进度通知栏
    fun createProgressNotification(context: Context, progress: Int) {
        if (fromO()) {
            val notificationChannel =
                NotificationChannel(
                    OTHER_CHANNEL_ID,
                    OTHER_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，
            //通知才能正常弹出
            mNotificationManager.createNotificationChannel(notificationChannel)
        }

        val notification = NotificationCompat.Builder(context, OTHER_CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher) // the status icon
            .setWhen(System.currentTimeMillis()) // the time stamp
            .setCustomContentView(createProgressView(context, progress))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        if (context is Service) {
            context.startForeground(musicNotify, notification)
        }else {
            mNotificationManager.notify(progressNotify, notification)
        }
    }


    private fun createProgressView(context: Context, progress: Int): RemoteViews {
        mProgressView = mProgressView ?: RemoteViews(
            context.packageName,
            R.layout.remote_layout_progress
        )
        mProgressView?.setProgressBar(R.id.pbDownload, 100, progress, false)
        mProgressView?.setTextViewText(R.id.tvProgressContent, "$progress%")
        return mProgressView!!
    }
}