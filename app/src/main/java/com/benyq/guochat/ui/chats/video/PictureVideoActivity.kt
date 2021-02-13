package com.benyq.guochat.ui.chats.video

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.databinding.ActivityPictureVideoBinding
import com.benyq.guochat.model.vm.PictureVideoViewModel
import com.benyq.guochat.model.vm.StateEvent
import com.benyq.module_base.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/5/10
 * @e-mail 1520063035@qq.com
 * @note 聊天界面 拍照或者摄像宿主
 */
@AndroidEntryPoint
class PictureVideoActivity : BaseActivity<ActivityPictureVideoBinding>() {

    private val pictureVideoViewModel: PictureVideoViewModel by viewModels()

    override fun provideViewBinding() = ActivityPictureVideoBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataObserver()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.flContainer, PictureVideoFragment(), "PictureVideoFragment")
                .commit()
        }
    }

    override fun isHideBar() = true

    override fun initImmersionBar() {
        super.initImmersionBar()
        // 保持Activity处于唤醒状态
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun initView() {
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_stay, R.anim.anim_capture_slide_bottom_out)
    }

    private fun dataObserver() {
        pictureVideoViewModel.mState.observe(this, Observer {
            when (it.state) {
                StateEvent.STATE_IMG -> {
                    val fragment = supportFragmentManager.findFragmentByTag("PictureVideoFragment")
                    navigate(fragment, PictureConfirmFragment(), Bundle().apply {
                        putString(IntentExtra.imgPath, it.path)
                    })
                }
                StateEvent.STATE_VIDEO -> {
                    val fragment = supportFragmentManager.findFragmentByTag("PictureVideoFragment")
                    navigate(fragment, VideoConfirmFragment(), Bundle().apply {
                        putString(IntentExtra.videoPath, it.path)
                        putInt(IntentExtra.videoDuration, it.videoDuration)
                    })
                }

                StateEvent.STATE_CLEAR_TOP -> {
                    onBackPressed()
                }
                StateEvent.STATE_CLEAR_ALL -> {
                    finish()
                }

                StateEvent.STATE_FINISH_IMG -> {
                    popBack(it.path!!)
                }

                StateEvent.STATE_FINISH_VIDEO -> {
                    popBack(it.path!!, StateEvent.STATE_FINISH_VIDEO, it.videoDuration)
                }
            }
        })
    }

    private fun navigate(last: Fragment?, dest: Fragment, bundle: Bundle) {
        dest.arguments = bundle
        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer, dest, dest.javaClass.simpleName)
            .apply {
                if (last != null) {
                    hide(last)
                }
            }
            .addToBackStack(null)
            .commit()
    }

    private fun popBack(path: String, state: Int = StateEvent.STATE_FINISH_IMG, duration: Int = 0) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(IntentExtra.videoImgPath, path)
            putExtra(IntentExtra.stateEvent, state)
            putExtra(IntentExtra.videoDuration, duration)
        })
        finish()
    }
}
