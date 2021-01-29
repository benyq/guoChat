package com.benyq.imageviewer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
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

    override fun onCreate(savedInstanceState: Bundle?) {
        immersionBar {
            hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
        }
        //取消动画
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_photo)

        initView()
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