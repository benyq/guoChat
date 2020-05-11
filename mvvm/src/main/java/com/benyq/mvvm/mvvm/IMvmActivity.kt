package com.benyq.mvvm.mvvm

import com.benyq.mvvm.base.IActivity

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
interface IMvmActivity : IActivity, IMvmView {

    fun dataObserver(){}
}