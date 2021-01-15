package com.benyq.mvvm.ui.base

/**
 *  author: HyJame
 *  date:   2019-12-03
 *  desc:   TODO
 */
interface IView {

    fun getLayoutId(): Int = 0

    fun initBefore() {}

    fun initView() {}

    fun initListener() {}

}