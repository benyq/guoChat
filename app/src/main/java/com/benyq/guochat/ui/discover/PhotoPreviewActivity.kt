package com.benyq.guochat.ui.discover

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.mvvm.ui.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_photo_preview.*

/**
 * @author benyq
 * @time 2021/1/8
 * @e-mail 1520063035@qq.com
 * @note 全屏显示图片，利用过渡动画实现效果
 */

class PhotoPreviewActivity : BaseActivity() {

    private val mAdapter = CirclePhotoAdapter()

    override fun getLayoutId() = R.layout.activity_photo_preview

    override fun isHideBar() = true

    override fun initView() {

        val photoUrls = intent.getStringArrayExtra(IntentExtra.circlePhotos) ?: emptyArray()
        val photoUrlsIndex = intent.getIntExtra(IntentExtra.circlePhotosIndex, 0)

        val child: View = photoPager.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        photoPager.adapter = mAdapter
        photoPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mAdapter.setNewInstance(photoUrls.toMutableList())

        photoPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

            }
        })

        photoPager.setCurrentItem(photoUrlsIndex, false)

        Glide.with(this).load(photoUrls[photoUrlsIndex]).into(ivCirclePhoto)
    }
}