package com.benyq.guochat.ui.base

import android.os.Bundle
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class LifecycleActivity<VM : BaseViewModel> : BaseActivity() {

    lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        mViewModel = initVM()
        dataObserver()
        super.onCreate(savedInstanceState)
    }

    abstract fun initVM(): VM
    abstract fun dataObserver()
}