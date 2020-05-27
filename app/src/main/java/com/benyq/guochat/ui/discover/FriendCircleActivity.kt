package com.benyq.guochat.ui.discover

import android.graphics.Color
import com.benyq.guochat.R
import com.benyq.guochat.loadAvatar
import com.benyq.guochat.local.LocalStorage
import com.benyq.guochat.ui.base.LifecycleActivity
import com.benyq.guochat.ui.common.widget.HeaderView
import com.benyq.mvvm.ext.loge
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_friend_circle.*
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/5/26
 * @e-mail 1520063035@qq.com
 * @note 果聊朋友圈，当前用户的
 */
class FriendCircleActivity : LifecycleActivity() {

    override fun getLayoutId() = R.layout.activity_friend_circle

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarView(R.id.toolbar)
            .autoDarkModeEnable(true, 0.2f)
            .init()
    }

    override fun initView() {
        ImmersionBar.setTitleBar(this, toolbar)

        val user = LocalStorage.userAccount
        loadAvatar(ivAvatar, user.avatarUrl)
        tvNickName.text = user.nickName
        Glide.with(this)
            .load("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=11232128,2567744034&fm=26&gp=0.jpg")
            .centerCrop().into(ivBg)
    }

    override fun initListener() {
        headerView.setBackAction { finish() }
        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            private var offset = 0
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                val offHeight: Int = (appBarLayout.totalScrollRange * 0.8).toInt()
                val percentage = abs(verticalOffset) * 1f / appBarLayout.totalScrollRange

                if (offset >= verticalOffset) {
                    //向上
                    if (percentage > 0.8) {
                        ivAvatar.alpha = 1 - percentage
                        tvNickName.alpha = 1 - percentage
                    }
                } else {
                    //向下
                    if (percentage < 0.8) {
                        ivAvatar.alpha = 1f
                        tvNickName.alpha = 1f
                    } else {
                        ivAvatar.alpha = 1 - percentage
                        tvNickName.alpha = 1 - percentage
                    }
                }
                offset = verticalOffset
                if (percentage == 1f) {
                    headerView.setHeaderViewMode(HeaderView.toolbarTypeNormal)
                    headerView.setToolbarTitle(getString(R.string.guo_chat_circle))
                }
                if (percentage == 0f) {
                    headerView.setHeaderViewMode(HeaderView.toolbarTypeDark)
                    headerView.setToolbarTitle("")
                }
                ImmersionBar.with(this@FriendCircleActivity).statusBarDarkFont(abs(verticalOffset) >= offHeight, 0.2f).init()
            }
        })

    }
}
