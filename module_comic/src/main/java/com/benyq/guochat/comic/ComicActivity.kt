package com.benyq.guochat.comic

import android.os.Bundle
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.benyq.guochat.comic.databinding.ComicActivityComicBinding
import com.benyq.guochat.comic.model.vm.ComicViewModel
import com.benyq.guochat.comic.ui.home.ComicHomeFragment
import com.benyq.guochat.comic.ui.shelf.ComicShelfFragment
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.gyf.immersionbar.ktx.immersionBar
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note 漫画模块入口
 */
@AndroidEntryPoint
class ComicActivity : LifecycleActivity<ComicViewModel, ComicActivityComicBinding>() {

    private val TAG = javaClass.simpleName

    override fun initVM(): ComicViewModel = getViewModel()

    override fun initImmersionBar() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.white)
            statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.bottomNavigationBar.selectTab(viewModelGet().mCurrentIndex)
    }

    override fun provideViewBinding() = ComicActivityComicBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()

        binding.bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.comic_ic_home, "首页"))
            .addItem(BottomNavigationItem(R.drawable.comic_ic_bookshelf, "收藏夹"))
            .setActiveColor(R.color.colorPrimary)
            .setInActiveColor(R.color.black)
            .setBarBackgroundColor(R.color.darkgrey)
            .setMode(BottomNavigationBar.MODE_FIXED)
            .initialise()


    }

    override fun dataObserver() {
        binding.bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                switchTab(position)
            }

        })
    }


    private fun switchTab(position: Int) {
        hideFragment()
        changeTab(position)
        viewModelGet().mCurrentIndex = position
    }

    private fun changeTab(position: Int) {
        val tag = "$TAG $position"
        supportFragmentManager.beginTransaction().let {
            val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (position) {
                0 -> {
                    ComicHomeFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
                1 -> {
                    ComicShelfFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
                else -> {
                    ComicHomeFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
            }
            it.show(fragment)
            it.commitAllowingStateLoss()
        }
    }

    /**
     * 隐藏所有的fragment
     * 修改背景
     */
    private fun hideFragment() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val tag = "$TAG ${viewModelGet().mCurrentIndex}"
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment != null && fragment.isVisible) {
            transaction.hide(fragment)
        }
        transaction.commit()
    }
}