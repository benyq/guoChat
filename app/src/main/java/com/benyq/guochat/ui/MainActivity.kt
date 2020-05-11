package com.benyq.guochat.ui

import android.Manifest
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.FragmentTransaction
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.benyq.guochat.R
import com.benyq.guochat.function.music.PlayMusicService
import com.benyq.guochat.function.music.PlayerController
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.function.permissionX.PermissionX
import com.benyq.guochat.function.zxing.android.CaptureActivity
import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.model.vm.MainViewModel
import com.benyq.guochat.ui.base.BaseActivity
import com.benyq.guochat.ui.chats.ChatAdapter
import com.benyq.guochat.ui.chats.ChatFragment
import com.benyq.guochat.ui.chats.ChatRecordAdapter
import com.benyq.guochat.ui.common.WebViewActivity
import com.benyq.guochat.ui.contracts.AddContractActivity
import com.benyq.guochat.ui.contracts.ContractsFragment
import com.benyq.guochat.ui.discover.DiscoverFragment
import com.benyq.guochat.ui.me.MeFragment
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.*
import com.benyq.mvvm.mvvm.IMvmActivity
import com.github.promeg.pinyinhelper.Pinyin
import com.luck.picture.lib.immersive.ImmersiveManage
import kotlinx.android.synthetic.main.activity_main.*
import qiu.niorgai.StatusBarCompat


class MainActivity : BaseActivity(), IMvmActivity {

    @BindViewModel
    lateinit var mViewModel: MainViewModel
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
    }

    override fun initView() {

        bottomNavigationBar
            .addItem(BottomNavigationItem(R.drawable.ic_chat, titleArray[0]))
            .addItem(BottomNavigationItem(R.drawable.ic_contracts,  titleArray[1]))
            .addItem(BottomNavigationItem(R.drawable.ic_discover,  titleArray[2]))
            .addItem(BottomNavigationItem(R.drawable.ic_me,  titleArray[3]))
            .setActiveColor(R.color.colorPrimary)
            .setInActiveColor(R.color.black)
            .setBarBackgroundColor(R.color.darkgrey)
            .setMode(BottomNavigationBar.MODE_FIXED)
            .initialise()
    }

    override fun initListener() {
        bottomNavigationBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener{
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

    private fun createMorePopWindow() : PopupWindow{
        val view = View.inflate(this, R.layout.popup_more_function, null).apply {
            findViewById<LinearLayout>(R.id.llAddGroupChat)?.setOnClickListener {
                mMoreFunctionPop.dismiss()
//                PlayerController.playAudio("http://img.owspace.com/F_vne408361_1512551681.4364588.mp3")
                  WebViewActivity.gotoWeb(this@MainActivity, "https://www.jianshu.com/p/40fe79d65781", "简书")

            }
            findViewById<LinearLayout>(R.id.llAddContract)?.setOnClickListener {
                startActivity<AddContractActivity>()
                mMoreFunctionPop.dismiss()
            }
            findViewById<LinearLayout>(R.id.llScan)?.setOnClickListener {
                mMoreFunctionPop.dismiss()
                PermissionX.request(this@MainActivity, Manifest.permission.CAMERA) { allGranted, denyList ->
                    if (allGranted) {
                        startActivity<CaptureActivity>()
                    }else {
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
}
