
package com.benyq.guochat.function.music

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.view.KeyEvent
import com.benyq.mvvm.ext.loge

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note
 */
class PlayerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        loge("action ${intent.action}")
        if (intent.action == Intent.ACTION_MEDIA_BUTTON) {
            if (intent.extras == null) {
                return
            }
            val keyEvent =
                intent.extras!![Intent.EXTRA_KEY_EVENT] as KeyEvent? ?: return
            if (keyEvent.action != KeyEvent.ACTION_DOWN) {
                return
            }
            when (keyEvent.keyCode) {
                KeyEvent.KEYCODE_HEADSETHOOK, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE -> PlayerController.togglePlay()
                KeyEvent.KEYCODE_MEDIA_PLAY -> PlayerController.play()
                KeyEvent.KEYCODE_MEDIA_PAUSE -> PlayerController.playPause()
                KeyEvent.KEYCODE_MEDIA_STOP -> PlayerController.stop()
                KeyEvent.KEYCODE_MEDIA_NEXT -> PlayerController.playNext()
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> PlayerController.playPrevious()
                else -> {
                }
            }
        } else {
            when(intent.action) {
                PlayMusicService.ACTION_PLAY_TOGGLE -> PlayerController.togglePlay()
                PlayMusicService.ACTION_PLAY_PLAY -> PlayerController.play()
                PlayMusicService.ACTION_PLAY_PAUSE, AudioManager.ACTION_AUDIO_BECOMING_NOISY -> PlayerController.playPause()
                PlayMusicService.ACTION_PLAY_NEXT -> PlayerController.playNext()
                PlayMusicService.ACTION_PLAY_LAST -> PlayerController.playPrevious()
                PlayMusicService.ACTION_STOP_SERVICE -> PlayerController.stop()
            }
        }
    }
}