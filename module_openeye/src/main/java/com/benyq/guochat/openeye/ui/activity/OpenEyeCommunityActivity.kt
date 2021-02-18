package com.benyq.guochat.openeye.ui.activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.benyq.guochat.openeye.R
import com.benyq.guochat.openeye.databinding.ActivityCommunityBinding
import com.benyq.guochat.openeye.ui.fragment.DailyPaperFragment
import com.benyq.guochat.openeye.ui.fragment.OpenEyeSearchFragment
import com.benyq.guochat.openeye.ui.fragment.RecommendFragment
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ext.setTextStyleSelectState
import com.benyq.module_base.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note 开眼app其中的一个功能
 */
@AndroidEntryPoint
@Route(path = RouterPath.OPEN_EYE_COMMUNITY)
class OpenEyeCommunityActivity : BaseActivity<ActivityCommunityBinding>() {

    override fun provideViewBinding() = ActivityCommunityBinding.inflate(layoutInflater)

    override fun initView() {
        val titleArray = arrayOf("推荐", "日报")
        binding.viewPagerCommunity.adapter =
            object : FragmentStatePagerAdapter(supportFragmentManager) {

                override fun getItem(position: Int): Fragment {
                    return if (position == 0) {
                        RecommendFragment()
                    } else {
                        DailyPaperFragment()
                    }
                }

                override fun getCount() = titleArray.size

                override fun getPageTitle(position: Int) = titleArray[position]
            }


        binding.tabLayoutCommunity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.tabLayoutCommunity.setTextStyleSelectState(tab.position,
                    R.style.tab_style_bold
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                binding.tabLayoutCommunity.setTextStyleSelectState(tab.position,
                    R.style.tab_style_normal
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        binding.tabLayoutCommunity.setupWithViewPager( binding.viewPagerCommunity)

        binding.tabLayoutCommunity.setTextStyleSelectState(0, R.style.tab_style_bold)

        binding.ivSearch.setOnClickListener {
            showSearchFragment()
        }
    }


    private fun showSearchFragment() {
        OpenEyeSearchFragment.switchFragment(this)
    }
}