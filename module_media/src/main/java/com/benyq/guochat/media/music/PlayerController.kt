package com.benyq.guochat.media.music

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.calculateTime
import com.benyq.module_base.ext.isConnected
import java.lang.ref.WeakReference
import kotlin.properties.Delegates

/**
 * @author benyq
 * @time 2020/4/12
 * @e-mail 1520063035@qq.com
 * @note
 */
object PlayerController : IPlayController {

    private var currentIndex = 0

    private var playMode = PlayMode.LOOP_SINGLE

    private var mPlayMusic: PlayingMusic = PlayingMusic()

    val mPlayMusicLiveData = MutableLiveData<PlayingMusic>()
    val mPauseLiveData = MutableLiveData<Boolean>()

    private var isPaused by Delegates.observable(true) { property, oldValue, newValue ->
        mPauseLiveData.value = newValue
    }

    private var mWeakReference: WeakReference<Context>? = null
    private val mContext: Context?
        get() = mWeakReference?.get()

    override fun playNext() {
//        currentIndex = when(playMode) {
//            PlayMode.LOOP_ALL , PlayMode.LOOP_SINGLE-> {
//                val index = currentIndex + 1
//                index % mSongs.size
//            }
//            PlayMode.SHUFFLE -> {
//                (Math.random() * mSongs.size).toInt()
//            }
//        }
//        playAudio(mSongs[currentIndex].path)
    }

    override fun playPrevious() {
    }

    override fun playAgain() {
//        playAudio(mSongs[currentIndex].path)
    }

    override fun playPause() : Boolean{
        if (MediaPlayerHelper.player.isPlaying) {
            MediaPlayerHelper.player.pause()
            isPaused = true
            notifyService(true)
            return true
        }
        return false
    }

    override fun start(): Boolean {
        if (isPaused) {
            MediaPlayerHelper.player.start()
            return true
        }
        return false
    }

    override fun setSeek(progress: Int) {
        MediaPlayerHelper.player.seekTo(progress)
    }

    override fun getTrackTime() = calculateTime(MediaPlayerHelper.player.currentPosition / 1000)

    override fun changeMode() {
    }

    override fun play() : Boolean{
        if (isPaused) {
            MediaPlayerHelper.player.start()
            isPaused = false
            notifyService(true)
            return true
        }
        return false
    }

    fun togglePlay() {
        if (isPlaying()) {
            playPause()
        } else {
            play()
        }
    }

    fun setContext(context: Context) {
        mWeakReference = WeakReference(context)
    }

    /**
     * stop也只是暂停
     */
    fun stop() {
        MediaPlayerHelper.player.pause()
        isPaused = true
        mPauseLiveData.value = true
        notifyService(false)
    }

    fun release() {
        mWeakReference?.clear()
        mWeakReference = null
        MediaPlayerHelper.release()
        isPaused = true
        mPauseLiveData.value = true
        notifyService(false)
    }

    private fun notifyService(bool: Boolean) {
        if (bool) {
            mContext?.let {
                it.startService(Intent(it, PlayMusicService::class.java))
            }
        }else {
            mContext?.let {
                it.stopService(Intent(it, PlayMusicService::class.java))
            }
        }
    }


    fun playAudio(url: String?) {
        if (url.isNullOrEmpty()) {
            playPause()
        }else {
            if (url.contains("http:") || url.contains("ftp:") || url.contains("https:")) {
                if (mContext?.isConnected() == true) {
                    MediaPlayerHelper.play(url)
//                    afterPlay(context)
                    bindProgressListener()
                } else {
                    Toasts.show("网络未连接")
                }
            } else if (url.contains("storage")) {
                MediaPlayerHelper.play(url)
//                afterPlay(context)
                bindProgressListener()
            }
        }
    }

    fun isPlaying() = MediaPlayerHelper.player.isPlaying

    private fun bindProgressListener() {
        mPauseLiveData.value = !isPaused
        MediaPlayerHelper.setMediaPlayerHelperCallBack(object :
            MediaPlayerHelper.MediaPlayerHelperCallBack {
            override fun onCallBack(
                state: MusicPlayState,
                mediaPlayerHelper: MediaPlayerHelper?,
                vararg args: Any?
            ) {
                if (state == MusicPlayState.PROGRESS) {
                    val position = MediaPlayerHelper.player.currentPosition
                    val duration = MediaPlayerHelper.player.duration
                    mPlayMusic.nowTime = calculateTime(position / 1000)
                    mPlayMusic.allTime = calculateTime(duration / 1000)
                    mPlayMusic.duration = duration
                    mPlayMusic.playerPosition = position
                    mPlayMusicLiveData.value = mPlayMusic
                    if (mPlayMusic.nowTime == mPlayMusic.allTime || duration - position < 2000) {
                        if (playMode == PlayMode.LOOP_SINGLE) {
                            playAgain()
                        } else {
                            playNext()
                        }
                    }
                } else if (state == MusicPlayState.PREPARE) {
                    notifyService(true)
                }
            }
        })
    }

}