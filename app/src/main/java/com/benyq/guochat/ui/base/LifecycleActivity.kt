package com.benyq.guochat.ui.base

import com.benyq.mvvm.mvvm.IMvmActivity

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class LifecycleActivity : BaseActivity(), IMvmActivity {

    override fun hideLoading() {
        loadingHide()
    }

    override fun showLoading(content: String?) {
        loadingShow(content)
    }
}