package com.benyq.guochat.ui.me

import com.benyq.guochat.R
import com.benyq.guochat.ui.base.LifecycleActivity
import com.gyf.immersionbar.ktx.immersionBar
import kotlinx.android.synthetic.main.activity_avatar.*

/**
 * @author benyq
 * @time 2020/5/21
 * @e-mail 1520063035@qq.com
 * @note 头像显示与修改
 */
class AvatarActivity : LifecycleActivity() {

    override fun initWidows() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.color_2a2a2a)
        }
    }

    override fun getLayoutId() = R.layout.activity_avatar

    override fun initView() {
        //从本地缓存中取出user信息
        //Glide加载头像

    }

    override fun initListener() {
        headerView.setBackAction { finish() }
        headerView.setMenuAction {
            AvatarMenuDialog().run {
                setActionSelect { path, view ->
                    //上传头像，然后再显示
                }
                show(supportFragmentManager)
            }
        }
    }
}
