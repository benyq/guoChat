package com.benyq.guochat.ui.chats

import android.media.MediaPlayer
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.calculateTime
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.ext.gone
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.visible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_chat_video.*
import kotlinx.coroutines.*


/**
 * @author benyq
 * @time 2020/5/17
 * @e-mail 1520063035@qq.com
 * @note 小视频播放
 */
class ChatVideoActivity : BaseActivity() {

    private var mVideoPaused = true
    private var mVideoPrepared = false
    private val mVideoProgressJob = Job()
    private lateinit var videoPath: String
    private val launch = CoroutineScope(mVideoProgressJob)


    override fun initWidows() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    override fun getLayoutId() = R.layout.activity_chat_video

    override fun initView() {
        videoPath = intent.getStringExtra(IntentExtra.videoPath) ?: ""
        getFrameAtTime(videoPath)

        videoView.setVideoPath(videoPath)
        videoView.setOnPreparedListener {
            tvVideoDuration.text = calculateTime(it.duration / 1000)
            tvCurrentPosition.text = calculateTime(0)

            sbVideo.max = it.duration
            loge("视频已经准备就绪")
            mVideoPrepared = true
        }
        videoView.setOnErrorListener { mp, what, extra ->
            Toasts.show("视频播放错误")
            true
        }
        videoView.setOnCompletionListener {
            videoView.resume()
            checkState(false)
            sbVideo.progress = 0
            tvCurrentPosition.text = calculateTime(0)
            ivFirstFrame.visible()
            mVideoPaused = true
        }
        videoView.setOnInfoListener { mp, what, extra ->
            loge("开始播放 $what")
            if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                ivFirstFrame.gone()
            } else if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {

            }
            false
        }
    }

    override fun initListener() {
        ivVideoClose.setOnClickListener {
            finish()
        }

        val playListener = View.OnClickListener {
            if (!mVideoPrepared) {
                Toasts.show("正在加载小视频")
                return@OnClickListener
            }
            if (mVideoPaused) {
                videoView.start()
                calculateVideoProgress()
                checkState(true)
            } else {
                videoView.pause()
                checkState(false)
            }
            mVideoPaused = !mVideoPaused
        }
        ivTogglePlay.setOnClickListener(playListener)
        ivVideoFill.setOnClickListener(playListener)

        sbVideo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                videoView.seekTo(sbVideo.progress)
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        mVideoPaused = true
        mVideoProgressJob.cancel()
    }


    /**
     * flag true 开始播放 false 暂停
     */
    private fun checkState(flag: Boolean) {
        if (flag) {
            ivVideoFill.gone()
            ivTogglePlay.setImageResource(R.drawable.ic_remote_view_play_white)
        } else {
            ivVideoFill.visible()
            ivTogglePlay.setImageResource(R.drawable.ic_remote_view_pause_white)
        }
    }

    private fun calculateVideoProgress() {
        launch.launch(Dispatchers.Main) {
            while (!mVideoPaused) {
                val position = videoView.currentPosition
                tvCurrentPosition.text = calculateTime(position / 1000)
                sbVideo.progress = position
                delay(1000)
            }
        }
    }

    /**
     * 获取视频第一张画面
     */
    private fun getFrameAtTime(videoPath: String) {
        //废弃，因为Glide有缓存的
//        launch.launch {
//            val mmr = MediaMetadataRetriever()
//            mmr.setDataSource(videoPath)
//            val bitmap = mmr.frameAtTime //获得视频第一帧的Bitmap对象
//            withContext(Dispatchers.Main) {
//                if (bitmap != null) {
//                    ivFirstFrame.setImageBitmap(bitmap)
//                }
//            }
//        }
        Glide.with(this).load(videoPath).into(ivFirstFrame)
    }
}
