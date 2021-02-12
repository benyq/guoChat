package com.benyq.module_base.ui.base

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseApplication : Application(), ViewModelStoreOwner {

    private val mAppViewModelStore: ViewModelStore = ViewModelStore()

    override fun getViewModelStore() = mAppViewModelStore

}