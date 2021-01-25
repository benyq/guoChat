package com.benyq.guochat.ui.chats

import android.media.MediaPlayer
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.SeekBar
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.calculateTime
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.gyf.immersionbar.ImmersionBar
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
    private lateinit var videoPath: String

    override fun isHideBar() = true

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

        resizeViewMargin()
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

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.alpha_scale_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoPaused = true
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
        lifecycleScope.launch(Dispatchers.Main) {
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
        Glide.with(this)
            .load(videoPath)
            .placeholder(R.color.black).into(ivFirstFrame)
    }

    private fun resizeViewMargin() {

        if (checkFullScreenPhone()) {
            val topMargin = dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCloseParam = ivVideoClose.layoutParams as FrameLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            ivVideoClose.layoutParams = ivCloseParam
        }
    }
}
