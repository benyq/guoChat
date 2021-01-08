package com.benyq.guochat.ui.discover

import android.app.Activity
import android.content.Intent
import android.opengl.Matrix
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.mvvm.ui.base.BaseActivity
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_circle_photo_view_pager.*


/**
 * @author benyq
 * @time 2020/7/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class CirclePhotoViewPagerActivity : BaseActivity() {

    private val mAdapter = CirclePhotoAdapter()

    override fun getLayoutId() = R.layout.activity_circle_photo_view_pager

    override fun initView() {
        headerView.run {
            setBackAction {
                returnPhotoData()
            }
            setMenuAction {
                mAdapter.removeAt(viewPager.currentItem)
                if (mAdapter.data.isNotEmpty()) {
                    headerView.setToolbarTitle("${viewPager.currentItem + 1} / ${mAdapter.data.size}")
                }else {
                    returnPhotoData()
                }
            }
        }

        val photoUrls = intent.getStringArrayExtra(IntentExtra.circlePhotos) ?: emptyArray()
        val photoUrlsIndex = intent.getIntExtra(IntentExtra.circlePhotosIndex, 0)

        val child: View = viewPager.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        viewPager.adapter = mAdapter
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mAdapter.setNewInstance(photoUrls.toMutableList())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mAdapter.data.isEmpty()) {
                    headerView.setToolbarTitle("0 / 0")
                }else {
                    headerView.setToolbarTitle("${position + 1} / ${mAdapter.data.size}")
                }
            }
        })

        viewPager.setCurrentItem(photoUrlsIndex, false)

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                returnPhotoData()
            }
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_stay, R.anim.slide_bottom_out)
    }

    private fun returnPhotoData() {
        val data = Intent()
        data.putExtra(IntentExtra.circlePhotos, mAdapter.data.toTypedArray())
        setResult(Activity.RESULT_OK, data)
        finish()
    }
}