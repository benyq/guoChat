package com.benyq.guochat.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.mvvm.ui.base.BaseActivity
import com.bumptech.glide.Glide
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_photo_preview.*
import kotlinx.android.synthetic.main.item_circle_photo.view.*

/**
 * @author benyq
 * @time 2021/1/8
 * @e-mail 1520063035@qq.com
 * @note 全屏显示图片，利用过渡动画实现效果
 */

class PhotoPreviewActivity : BaseActivity() {

    private lateinit var mPageAdapter: PagerAdapter

    override fun getLayoutId() = R.layout.activity_photo_preview

    override fun isHideBar() = false

    override fun initImmersionBar() {
        immersionBar{
            hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
        }
    }

    override fun initView() {

        val photoUrls = intent.getStringArrayExtra(IntentExtra.circlePhotos) ?: emptyArray()
        val photoUrlsIndex = intent.getIntExtra(IntentExtra.circlePhotosIndex, 0)

        mPageAdapter = object : PagerAdapter() {

            override fun getCount() = photoUrls.size

            override fun isViewFromObject(view: View, o: Any) = view == o

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(`object` as View?)
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val view = LayoutInflater.from(this@PhotoPreviewActivity).inflate(R.layout.item_circle_photo, null)
                val photoView = view.ivCirclePhoto
                photoView.setBackgroundColor(Color.BLACK)
                Glide.with(this@PhotoPreviewActivity)
                    .load(photoUrls[position])
                    .into(photoView)
                container.addView(view)
                return view
            }
        }
        photoPager.adapter = mPageAdapter
        photoPager.offscreenPageLimit = photoUrls.size
        photoPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        photoPager.setCurrentItem(photoUrlsIndex, false)

    }

}