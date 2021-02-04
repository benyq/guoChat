package com.benyq.imageviewer

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ui.base.BaseActivity
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_preview_photo.*

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 预览 Activity
 */

class PreviewPhotoActivity : AppCompatActivity() {

    //当前的index
    private var mCurrentIndex = 0
    private val mViewModel by lazy { ViewModelProvider(this).get(PreviewViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Components.isFullScreen) {
            immersionBar {
                hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
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
            vpPreview.isUserInputEnabled = it
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }

    private fun initView() {
        mCurrentIndex = Components.curPosition

        vpPreview.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vpPreview.offscreenPageLimit = 1
        vpPreview.adapter = PreviewPageAdapter(supportFragmentManager, lifecycle, Components.data)
        vpPreview.setCurrentItem(mCurrentIndex, false)
        vpPreview.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                mCurrentIndex = position
                Components.curPosition = position
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        Components.release()
    }
}