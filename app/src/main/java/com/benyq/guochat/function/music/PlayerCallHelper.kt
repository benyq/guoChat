package com.benyq.guochat.function.music

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.AudioManager.OnAudioFocusChangeListener
import android.media.MediaMetadataRetriever
import android.media.RemoteControlClient
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note 在来电时自动协调和暂停音乐播放
 */
class PlayerCallHelper(val playerCallHelperListener: PlayerCallHelperListener) :
    OnAudioFocusChangeListener {

    private var phoneStateListener: PhoneStateListener? = null
    private var mAudioManager: AudioManager? = null
    private var remoteControlClient: RemoteControlClient? = null


    private var ignoreAudioFocus = false
    private var mIsTempPauseByPhone = false
    private var tempPause = false

    override fun onAudioFocusChange(focusChange: Int) {
        if (ignoreAudioFocus) {
            ignoreAudioFocus = false
            return
        }
        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            if (playerCallHelperListener.isPlaying() &&
                !playerCallHelperListener.isPaused()
            ) {
                playerCallHelperListener.pauseAudio()
                tempPause = true
            }
        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            if (tempPause) {
                playerCallHelperListener.playAudio()
                tempPause = false
            }
        }
    }

    fun bindCallListener(context: Context) {
        phoneStateListener = object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                if (state == TelephonyManager.CALL_STATE_IDLE) {
                    if (mIsTempPauseByPhone) {
                        playerCallHelperListener.playAudio()
                        mIsTempPauseByPhone = false
                    }
                } else if (state == TelephonyManager.CALL_STATE_RINGING) {
                    if (playerCallHelperListener.isPlaying() &&
                        !playerCallHelperListener.isPaused()
                    ) {
                        playerCallHelperListener.pauseAudio()
                        mIsTempPauseByPhone = true
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                }
                super.onCallStateChanged(state, phoneNumber)
            }
        }
        val manager: TelephonyManager? =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        manager?.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    fun bindRemoteController(context: Context) {
        mAudioManager =
            context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val remoteComponentName = ComponentName(context, PlayerReceiver::class.java.name)
        try {
            remoteControlClient = remoteControlClient ?: run {
                mAudioManager?.registerMediaButtonEventReceiver(remoteComponentName)
                val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
                mediaButtonIntent.component = remoteComponentName
                val mediaPendingIntent = PendingIntent.getBroadcast(
                    context, 0, mediaButtonIntent, 0
                )
                RemoteControlClient(mediaPendingIntent).apply {
                    mAudioManager?.registerRemoteControlClient(remoteControlClient)
                }
            }

            remoteControlClient?.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY
                        or RemoteControlClient.FLAG_KEY_MEDIA_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE
                        or RemoteControlClient.FLAG_KEY_MEDIA_STOP
                        or RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS
                        or RemoteControlClient.FLAG_KEY_MEDIA_NEXT
            )
        } catch (e: Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindCallListener(context: Context) {
        try {
            val mgr =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        } catch (e: java.lang.Exception) {
            Log.e("tmessages", e.toString())
        }
    }

    fun unbindRemoteController() {
        remoteControlClient?.run {
            val metadataEditor = editMetadata(true)
            metadataEditor.clear()
            metadataEditor.apply()
            mAudioManager?.unregisterRemoteControlClient(remoteControlClient)
            mAudioManager?.abandonAudioFocus(this@PlayerCallHelper)
        }
    }


    fun requestAudioFocus(title: String?, summary: String?) {
        remoteControlClient?.run {
            val metadataEditor = editMetadata(true)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_ARTIST, summary)
            metadataEditor.putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title)
            metadataEditor.apply()
            mAudioManager?.requestAudioFocus(
                this@PlayerCallHelper,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            )
        }

    }

    interface PlayerCallHelperListener {
        fun playAudio()
        fun isPlaying(): Boolean
        fun isPaused(): Boolean
        fun pauseAudio()
    }
}