package com.benyq.guochat.media

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import com.benyq.guochat.media.music.PlayerController
import com.benyq.guochat.media.databinding.ActivityMusicPlayingBinding
import com.benyq.module_base.ui.base.BaseActivity

/**
 * @author benyq
 * @time 2020/5/6
 * @e-mail 1520063035@qq.com
 * @note 单纯就是音乐播放展示的
 */
class MusicPlayingActivity : BaseActivity<ActivityMusicPlayingBinding>() {

    private val albumRotate: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.album_rotate
        )
    }

    override fun provideViewBinding() = ActivityMusicPlayingBinding.inflate(layoutInflater)

    override fun initView() {
        PlayerController.mPauseLiveData.observe(this, Observer {
            if (it) {
                binding.ivPlayToggle.setImageResource(R.drawable.ic_remote_view_pause)
                binding.ivPlayingAlbum.clearAnimation()
            } else {
                binding.ivPlayToggle.setImageResource(R.drawable.ic_remote_view_play)
                binding.ivPlayingAlbum.startAnimation(albumRotate)
            }
        })
    }

    override fun initListener() {
        binding.headerView.setBackAction { finish() }
        binding.btnPlayToggle.setOnClickListener {
            PlayerController.togglePlay()
        }


    }
}
