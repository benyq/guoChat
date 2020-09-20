package com.benyq.mvvm.ui.base

import android.os.Bundle
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class LifecycleActivity<VM : BaseViewModel> : BaseActivity() {

    private lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        isLifeCircle = true
        super.onCreate(savedInstanceState)
        dataObserver()
        initData()
    }

    abstract fun initVM(): VM
    abstract fun dataObserver()

    open fun viewModelGet(): VM {
        if (!this::mViewModel.isInitialized) {
            mViewModel = initVM()
        }
        return mViewModel
    }
}