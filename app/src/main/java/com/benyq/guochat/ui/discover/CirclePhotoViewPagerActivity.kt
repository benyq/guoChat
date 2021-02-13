package com.benyq.guochat.ui.discover

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.R
import com.benyq.guochat.app.IntentExtra
import com.benyq.guochat.databinding.ActivityCirclePhotoViewPagerBinding
import com.benyq.module_base.ui.base.BaseActivity


/**
 * @author benyq
 * @time 2020/7/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class CirclePhotoViewPagerActivity : BaseActivity<ActivityCirclePhotoViewPagerBinding>() {

    private val mAdapter = CirclePhotoAdapter()

    override fun provideViewBinding() = ActivityCirclePhotoViewPagerBinding.inflate(layoutInflater)

    override fun initView() {
        binding.headerView.run {
            setBackAction {
                returnPhotoData()
            }
            setMenuAction {
                mAdapter.removeAt(binding.viewPager.currentItem)
                if (mAdapter.data.isNotEmpty()) {
                    binding.headerView.setToolbarTitle("${binding.viewPager.currentItem + 1} / ${mAdapter.data.size}")
                }else {
                    returnPhotoData()
                }
            }
        }

        val photoUrls = intent.getStringArrayExtra(IntentExtra.circlePhotos) ?: emptyArray()
        val photoUrlsIndex = intent.getIntExtra(IntentExtra.circlePhotosIndex, 0)

        val child: View = binding.viewPager.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        binding.viewPager.adapter = mAdapter
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        mAdapter.setNewInstance(photoUrls.toMutableList())

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (mAdapter.data.isEmpty()) {
                    binding.headerView.setToolbarTitle("0 / 0")
                }else {
                    binding.headerView.setToolbarTitle("${position + 1} / ${mAdapter.data.size}")
                }
            }
        })

        binding.viewPager.setCurrentItem(photoUrlsIndex, false)

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