package com.benyq.guochat.function.music

import android.media.MediaPlayer
import android.os.Handler
import com.benyq.mvvm.ext.runUnUiThread
import com.benyq.mvvm.ext.tryCatch

/**
 * @author benyq
 * @time 2020/5/5
 * @e-mail 1520063035@qq.com
 * @note  音乐播放器
 */
object MediaPlayerHelper : MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    var player: MediaPlayer = initPlayer()

    private const val delaySecondTime = 1000L
    private val refreshTimeHandler = Handler()
    private var refreshTimeRunnable : Runnable = object : Runnable {
        override fun run() {
            refreshTimeHandler.removeCallbacks(this)
            if (player.isPlaying) {
                callBack(
                    MusicPlayState.PROGRESS,
                    100 * player.currentPosition / player.duration
                )
            }
            refreshTimeHandler.postDelayed(this, delaySecondTime)
        }

    }

    /**
     * mediaPlayer是否已经释放
     */
    private var isReleased = false

    private var mediaPlayerHelperCallBack: MediaPlayerHelperCallBack? = null

    override fun onCompletion(mp: MediaPlayer?) {

    }

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
        refreshTimeHandler.postDelayed(refreshTimeRunnable, delaySecondTime)
        callBack(MusicPlayState.PREPARE)
    }

    override fun onSeekComplete(mp: MediaPlayer?) {
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        callBack(MusicPlayState.ERROR)
        return false
    }


    fun play(url: String) : Boolean{
        if (isReleased) {
            isReleased = false
            player = initPlayer()
        }
        try {
            player.reset()
            player.setDataSource(url)
            player.prepareAsync()
        }catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    /**
     * 设置回调
     *
     * @param MediaPlayerHelperCallBack 回调
     * @return 类对象
     */
    fun setMediaPlayerHelperCallBack(MediaPlayerHelperCallBack: MediaPlayerHelperCallBack) {
        this.mediaPlayerHelperCallBack = MediaPlayerHelperCallBack
    }

    fun release() {
        player.release()
        isReleased = true
        refreshTimeHandler.removeCallbacks(refreshTimeRunnable)
    }

    private fun initPlayer() : MediaPlayer {
        return MediaPlayer().apply {
            setOnCompletionListener(this@MediaPlayerHelper)
            setOnPreparedListener(this@MediaPlayerHelper)
            setOnSeekCompleteListener(this@MediaPlayerHelper)
            setOnErrorListener(this@MediaPlayerHelper)
        }
    }

    /**
     * 统一回调
     *
     * @param state 状态
     * @param args  若干参数
     */
    private fun callBack(state: MusicPlayState, vararg args: Any) {
        mediaPlayerHelperCallBack?.onCallBack(state, this, args)
    }

    /**
     * 回调接口
     */
    interface MediaPlayerHelperCallBack {
        /**
         * 状态回调
         *
         * @param state             状态
         * @param mediaPlayerHelper MediaPlayer
         * @param args              若干参数
         */
        fun onCallBack(
            state: MusicPlayState,
            mediaPlayerHelper: MediaPlayerHelper?,
            vararg args: Any?
        )
    }
}