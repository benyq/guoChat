package com.benyq.guochat.ui.discover

import com.benyq.guochat.R
import com.benyq.guochat.ui.base.LifecycleFragment
import com.benyq.mvvm.ext.startActivity
import kotlinx.android.synthetic.main.fragment_discover.*

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class DiscoverFragment : LifecycleFragment() {

    override fun getLayoutId() = R.layout.fragment_discover

    override fun initListener() {
        ifFriendCircle.setOnClickListener {
            startActivity<FriendCircleActivity>()
        }
    }
}
