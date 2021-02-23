package com.benyq.guochat.chat.ui

import android.Manifest
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.ActivityMainBinding
import com.benyq.guochat.chat.function.zxing.android.CaptureActivity
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.chat.model.vm.MainViewModel
import com.benyq.module_base.ui.base.LifecycleActivity
import com.benyq.guochat.chat.ui.chats.ChatFragment
import com.benyq.module_base.ui.WebViewActivity
import com.benyq.guochat.chat.ui.contracts.AddContractActivity
import com.benyq.guochat.chat.ui.contracts.ContractsFragment
import com.benyq.guochat.chat.ui.discover.DiscoverFragment
import com.benyq.guochat.chat.ui.me.MeFragment
import com.benyq.guochat.media.music.PlayerController
import com.benyq.module_base.ext.*
import com.gyf.immersionbar.ktx.immersionBar
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : LifecycleActivity<MainViewModel, ActivityMainBinding>() {

    override fun initVM(): MainViewModel = getViewModel()

    private val titleArray = arrayOf("聊天", "联系人", "发现", "我")

    /**
     * 功能弹窗
     */
    private val mMoreFunctionPop by lazy { createMorePopWindow() }

    override fun provideViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ChatObjectBox.testAddChatFromTo()
        PlayerController.setContext(this)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            private var tapTime = 0L
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - tapTime > 2000) {
                    tapTime = System.currentTimeMillis()
                    Toasts.show("再点击一次退出")
                } else {
                    finish()
                }
            }
        })

        binding.bottomNavigationBar.selectTab(viewModelGet().mCurrentIndex)
    }

    override fun initImmersionBar() {
        immersionBar {
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(
                true,
                0.2f
            ) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    override fun initView() {
        binding.bottomNavigationBar
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


        binding.toolbarAdd.setOnClickListener {
            val widthX = getScreenWidth() - mMoreFunctionPop.contentView.measuredWidth - 10
            mMoreFunctionPop.showAsDropDown(binding.toolbar, widthX, 10)
        }

    }


    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        PlayerController.release()
    }

    private fun switchTab(position: Int) {
        binding.toolbarTitle.text = titleArray[position]
        hideFragment()
        changeTab(position)
        viewModelGet().mCurrentIndex = position
    }

    private fun changeTab(position: Int) {
        val tag = "mainFragment $position"
        supportFragmentManager.beginTransaction().let {
            val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (position) {
                0 -> {
                    ChatFragment().apply {
                        it.add(R.id.flContainer, this, tag)
                    }
                }
                1 -> {
                    ContractsFragment().apply {
                        it.add(R.id.flContainer, this, tag)
                    }
                }
                2 -> {
                    DiscoverFragment().apply {
                        it.add(R.id.flContainer, this, tag)
                    }
                }
                else -> {
                    MeFragment().apply {
                        it.add(R.id.flContainer, this, tag)
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
        val tag = "mainFragment ${viewModelGet().mCurrentIndex}"
        val fragment = supportFragmentManager.findFragmentByTag(tag)

        if (fragment != null && fragment.isVisible) {
            transaction.hide(fragment)
        }
        transaction.commit()
    }

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
                XXPermissions.with(this@MainActivity)
                    .permission(Manifest.permission.CAMERA)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                            goToActivity<CaptureActivity>()
                        }

                        override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                            Toasts.show("权限拒绝")
                        }
                    })
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
