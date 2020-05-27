package com.benyq.guochat.ui.function

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.function.music.PlayerController
import com.benyq.guochat.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_music_playing.*

/**
 * @author benyq
 * @time 2020/5/6
 * @e-mail 1520063035@qq.com
 * @note 单纯就是音乐播放展示的
 */
class MusicPlayingActivity : BaseActivity() {

    private val albumRotate: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.album_rotate
        )
    }

    override fun getLayoutId() = R.layout.activity_music_playing

    override fun initView() {
        PlayerController.mPauseLiveData.observe(this, Observer {
            if (it) {
                ivPlayToggle.setImageResource(R.drawable.ic_remote_view_pause)
                ivPlayingAlbum.clearAnimation()
            } else {
                ivPlayToggle.setImageResource(R.drawable.ic_remote_view_play)
                ivPlayingAlbum.startAnimation(albumRotate)
            }
        })
    }

    override fun initListener() {
        headerView.setBackAction { finish() }
        btnPlayToggle.setOnClickListener {
            PlayerController.togglePlay()
        }


    }
}
