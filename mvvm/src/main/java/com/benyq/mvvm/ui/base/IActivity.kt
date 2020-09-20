package com.benyq.mvvm.ui.base

import android.os.Bundle

/**
 *  @author: HyJame
 *  @date:   2019-11-20
 *  @desc:   TODO
 */
interface IActivity: IView {

    fun initWidows() {}

    fun initArgs(extras: Bundle?): Boolean = true
}