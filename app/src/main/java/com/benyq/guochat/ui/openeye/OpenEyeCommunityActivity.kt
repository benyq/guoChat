package com.benyq.guochat.ui.openeye

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.benyq.guochat.R
import com.benyq.guochat.setTextStyleSelectState
import com.benyq.mvvm.ui.base.BaseActivity
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_community.*

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note 开眼app其中的一个功能
 */
@AndroidEntryPoint
class OpenEyeCommunityActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_community

    override fun initView() {
        val titleArray = arrayOf("推荐", "日报")
        viewPagerCommunity.adapter =
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


        tabLayoutCommunity.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tabLayoutCommunity.setTextStyleSelectState(tab.position, R.style.tab_style_bold)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tabLayoutCommunity.setTextStyleSelectState(tab.position, R.style.tab_style_normal)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        tabLayoutCommunity.setupWithViewPager(viewPagerCommunity)

        tabLayoutCommunity.setTextStyleSelectState(0, R.style.tab_style_bold)

        ivSearch.setOnClickListener {
            showSearchFragment()
        }
    }


    private fun showSearchFragment() {
        OpenEyeSearchFragment.switchFragment(this)
    }
}