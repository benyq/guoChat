package com.benyq.guochat.ui.base

import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.IMvmFragment

/**
 * @author benyq
 * @time 2020/4/20
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class LifecycleFragment : BaseFragment(), IMvmFragment {

    protected var TAG = this.javaClass.simpleName

    open var isFirstLoad = true

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
}