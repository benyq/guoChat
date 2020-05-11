package com.benyq.mvvm.delegate

import android.content.Context
import android.os.Bundle
import android.view.View

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
interface FragmentDelegate {

    fun onAttached(context: Context)

    fun onCreated(savedInstanceState: Bundle?)

    fun onViewCreated(v: View, savedInstanceState: Bundle?)

    fun onActivityCreate(savedInstanceState: Bundle?)

    fun onStarted()

    fun onResumed()

    fun onPaused()

    fun onStopped()

    fun onSaveInstanceState(outState: Bundle)

    fun onViewDestroyed()

    fun onDestroyed()

    fun onDetached()

    fun isAdd(): Boolean

}