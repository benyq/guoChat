package com.benyq.mvvm.delegate

import android.app.Activity
import android.os.Bundle

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
interface ActivityDelegate {

    fun onCreate(savedInstanceState: Bundle?)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onSaveInstanceState(activity: Activity?, outState: Bundle?)

    fun onDestroy()

}