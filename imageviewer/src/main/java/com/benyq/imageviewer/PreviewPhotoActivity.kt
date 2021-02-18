package com.benyq.imageviewer

import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.benyq.imageviewer.databinding.ActivityPreviewPhotoBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 预览 Activity
 */

internal class PreviewPhotoActivity : AppCompatActivity() {

    //当前的index
    private var mCurrentIndex = 0
    private val mViewModel by lazy { ViewModelProvider(this).get(PreviewViewModel::class.java) }
    private val binding by lazy { ActivityPreviewPhotoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Components.isFullScreen) {
            immersionBar {
                hideBar(BarHide.FLAG_HIDE_BAR)
            }
        }
        //取消动画
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_photo)

        initView()
        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(!mViewModel.isExiting) {
            override fun handleOnBackPressed() {
                //拦截了activity的退出，先执行玩动画再给我走
                mViewModel.setExitAnimPosition(mCurrentIndex)
                mViewModel.isExiting = true
            }
        })

        mViewModel.viewerUserInputEnabled.observe(this) {
            binding.vpPreview.isUserInputEnabled = it
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun initView() {
        mCurrentIndex = Components.curPosition

        binding.vpPreview.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            offscreenPageLimit = 1
            adapter = PreviewPageAdapter(supportFragmentManager, lifecycle, Components.data)
            setCurrentItem(mCurrentIndex, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mCurrentIndex = position
                    Components.curPosition = position
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Components.release()
    }
}