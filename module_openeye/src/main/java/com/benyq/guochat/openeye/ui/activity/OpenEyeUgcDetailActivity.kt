package com.benyq.guochat.openeye.ui.activity

import android.app.Activity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.openeye.IntentDataHolder
import com.benyq.guochat.openeye.R
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.module_base.ext.goToActivity
import com.benyq.guochat.openeye.databinding.ActivityEyeUgcDetailBinding
import com.benyq.guochat.openeye.model.bean.CommunityRecommend
import com.benyq.guochat.openeye.ui.fragment.FollowCardFragment
import com.benyq.guochat.openeye.model.vm.OpenEyeUgcDetailViewModel
import com.benyq.module_base.ext.overScrollNever
import com.gyf.immersionbar.ktx.immersionBar
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/9/7
 * @e-mail 1520063035@qq.com
 * @note  卡片页跳转过来的
 */
@AndroidEntryPoint
class OpenEyeUgcDetailActivity : LifecycleActivity<OpenEyeUgcDetailViewModel, ActivityEyeUgcDetailBinding>() {

    companion object {

        const val EXTRA_RECOMMEND_ITEM_LIST_JSON = "recommendItemList"
        const val EXTRA_RECOMMEND_ITEM_POSITION = "recommendItemPosition"

        fun start(context: Activity, dataList: List<CommunityRecommend.Item>, position: Int) {
            IntentDataHolder.setData(EXTRA_RECOMMEND_ITEM_LIST_JSON, dataList)
            IntentDataHolder.setData(EXTRA_RECOMMEND_ITEM_POSITION, position)
            context.goToActivity<OpenEyeUgcDetailActivity>(
                enterAnim = R.anim.slide_bottom_in,
                exitAnim = R.anim.slide_bottom_out
            )
        }
    }


    override fun initVM(): OpenEyeUgcDetailViewModel = getViewModel()

    override fun provideViewBinding() = ActivityEyeUgcDetailBinding.inflate(layoutInflater)

    override fun initImmersionBar() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.black)
            statusBarDarkFont(false, 1f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    override fun initView() {
        super.initView()

        binding.viewPager.overScrollNever()
        binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.viewPager.offscreenPageLimit = 1
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewModelGet().itemPosition = position
            }
        })
    }

    override fun initData() {
        super.initData()
        if (viewModelGet().dataList == null) {
            viewModelGet().dataList = IntentDataHolder.getData(EXTRA_RECOMMEND_ITEM_LIST_JSON)
            viewModelGet().itemPosition =
                IntentDataHolder.getData(EXTRA_RECOMMEND_ITEM_POSITION) ?: 0
        }
        if (viewModelGet().dataList == null) {
            finish()
        } else {
            binding.viewPager.adapter = object : FragmentStateAdapter(supportFragmentManager, lifecycle) {

                override fun getItemCount() = viewModelGet().dataList?.size ?: 0

                override fun createFragment(position: Int): Fragment {
                    return FollowCardFragment().apply {
                        arguments =
                            bundleOf(FollowCardFragment.EXTRA_RECOMMEND_ITEM_JSON to viewModelGet().dataList!![position])
                    }
                }
            }
            binding.viewPager.setCurrentItem(viewModelGet().itemPosition, false)
        }
    }

    override fun dataObserver() {

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, R.anim.slide_bottom_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        CacheFactory.getCacheManager().clearCache(this, null, null)
    }

}