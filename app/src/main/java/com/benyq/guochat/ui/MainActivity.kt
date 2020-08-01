package com.benyq.guochat.ui

import android.Manifest
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.benyq.guochat.R
import com.benyq.guochat.function.music.PlayerController
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.model.vm.MainViewModel
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.chats.ChatFragment
import com.benyq.guochat.ui.common.WebViewActivity
import com.benyq.guochat.ui.contracts.AddContractActivity
import com.benyq.guochat.ui.contracts.ContractsFragment
import com.benyq.guochat.ui.discover.DiscoverFragment
import com.benyq.guochat.ui.me.MeFragment
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.toast
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : LifecycleActivity<MainViewModel>() {

    override fun initVM(): MainViewModel = getViewModel()

    private val titleArray = arrayOf("聊天", "联系人", "发现", "我")

    /**
     * 功能弹窗
     */
    private val mMoreFunctionPop by lazy { createMorePopWindow() }

    override fun getLayoutId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ObjectBox.testAddChatFromTo()
        PlayerController.setContext(this)
        isSupportSwipeBack = false
    }

    override fun initImmersionBar() {
        immersionBar {
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    override fun initView() {

        bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.ic_chat, titleArray[0]))
            .addItem(BottomNavigationItem(R.drawable.ic_contracts, titleArray[1]))
            .addItem(BottomNavigationItem(R.drawable.ic_discover, titleArray[2]))
            .addItem(BottomNavigationItem(R.drawable.ic_me, titleArray[3]))
            .setActiveColor(R.color.colorPrimary)
            .setInActiveColor(R.color.black)
            .setBarBackgroundColor(R.color.darkgrey)
            .setMode(BottomNavigationBar.MODE_FIXED)
            .initialise()
    }

    override fun initListener() {
        bottomNavigationBar.setTabSelectedListener(object :
            BottomNavigationBar.OnTabSelectedListener {
            override fun onTabReselected(position: Int) {

            }

            override fun onTabUnselected(position: Int) {
            }

            override fun onTabSelected(position: Int) {
                switchTab(position)
            }

        })

        bottomNavigationBar.selectTab(mViewModel.mCurrentIndex)

        toolbarAdd.setOnClickListener {
            val widthX = getScreenWidth() - mMoreFunctionPop.contentView.measuredWidth - 10
            mMoreFunctionPop.showAsDropDown(toolbar, widthX, 10)
        }

    }


    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerController.release()
    }

    private fun switchTab(position: Int) {
        toolbarTitle.text = titleArray[position]
        hideFragment()
        changeTab(position)
        mViewModel.mCurrentIndex = position
    }

    private fun changeTab(position: Int) {
        val tag = "mainFragment $position"
        supportFragmentManager.beginTransaction().let {
            val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (position) {
                0 -> {
                    ChatFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
                1 -> {
                    ContractsFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
                2 -> {
                    DiscoverFragment().apply {
                        it.add(R.id.llContainer, this, tag)
                    }
                }
                else -> {
                    MeFragment().apply {
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
        val tag = "mainFragment ${mViewModel.mCurrentIndex}"
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment != null && fragment.isVisible) {
            transaction.hide(fragment)
        }
        transaction.commit()
    }

    var notifyId = 1

    private fun createMorePopWindow(): PopupWindow {
        val view = View.inflate(this, R.layout.popup_more_function, null).apply {
            findViewById<LinearLayout>(R.id.llAddGroupChat)?.setOnClickListener {
                mMoreFunctionPop.dismiss()
//                PlayerController.playAudio("http://img.owspace.com/F_vne408361_1512551681.4364588.mp3")
                WebViewActivity.gotoWeb(
                    this@MainActivity,
                    "https://www.jianshu.com/p/40fe79d65781",
                    "简书"
                )

            }
            findViewById<LinearLayout>(R.id.llAddContract)?.setOnClickListener {
                goToActivity<AddContractActivity>()
                mMoreFunctionPop.dismiss()
            }
            findViewById<LinearLayout>(R.id.llScan)?.setOnClickListener {
                mMoreFunctionPop.dismiss()
                PermissionX.request(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                ) { allGranted, denyList ->
                    if (allGranted) {
                        goToActivity<CaptureActivity>()
                    } else {
                        toast("权限拒绝")
                    }
                }
            }
        }
        view.measure(
            View.MeasureSpec.UNSPECIFIED,
            View.MeasureSpec.UNSPECIFIED
        )
        val popWindow = PopupWindow(view, view.measuredWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        popWindow.setBackgroundDrawable(ColorDrawable())

        popWindow.isOutsideTouchable = true
        return popWindow
    }

    override fun dataObserver() {
    }
}
