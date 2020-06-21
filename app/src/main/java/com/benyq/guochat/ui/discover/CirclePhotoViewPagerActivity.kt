package com.benyq.guochat.ui.discover

import android.app.Activity
import android.content.Intent
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_circle_photo_view_pager.*

/**
 * @author benyq
 * @time 2020/6/21
 * @e-mail 1520063035@qq.com
 * @note  朋友圈图片ViewPager2
 */
class CirclePhotoViewPagerActivity : BaseActivity() {

    private val mAdapter = CirclePhotoAdapter()

    override fun getLayoutId() = R.layout.activity_circle_photo_view_pager

    override fun initView() {

        headerView.run {
            setBackAction {
                val intent = Intent()
                intent.putExtra(IntentExtra.circlePhotos, mAdapter.data.toTypedArray())
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
            setMenuAction {
                mAdapter.removeAt(viewPager.currentItem)
                headerView.setToolbarTitle("${viewPager.currentItem + 1} / ${mAdapter.data.size}")
            }
        }

        isSupportSwipeBack = false

        val photoUrls = intent.getStringArrayExtra(IntentExtra.circlePhotos) ?: arrayOf()
        val photoUrlsIndex = intent.getIntExtra(IntentExtra.circlePhotosIndex, 0)
        viewPager.adapter = mAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mAdapter.setNewInstance(photoUrls.toMutableList())

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                headerView.setToolbarTitle("${position + 1} / ${mAdapter.data.size}")
            }
        })

        viewPager.setCurrentItem(photoUrlsIndex, false)
    }
}