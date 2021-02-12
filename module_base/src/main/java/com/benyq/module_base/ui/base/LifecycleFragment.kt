package com.benyq.module_base.ui.base

import android.os.Bundle
import android.view.View
import com.benyq.module_base.ext.loge
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class LifecycleFragment<VM : BaseViewModel> : BaseFragment(){

    lateinit var mViewModel: VM

    protected var TAG = this.javaClass.simpleName

    open var isFirstLoad = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = initVM()
        dataObserver()
        super.onViewCreated(view, savedInstanceState)
    }

    //ViewPaper2 懒加载
    override fun onResume() {
        super.onResume()
        if (isFirstLoad) {
            initData()
            isFirstLoad = false
        }
    }

    //  hide/show 数据刷新
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        loge("${TAG}onHiddenChanged${hidden}")
        if (!hidden) {
            initData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFirstLoad = true
    }

    abstract fun initVM(): VM
    abstract fun dataObserver()
}