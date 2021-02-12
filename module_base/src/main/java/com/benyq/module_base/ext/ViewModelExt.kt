package com.benyq.module_base.ext

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */


inline fun <reified VM : ViewModel> ComponentActivity.getViewModel(): VM {
    return ViewModelProvider(this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.getViewModel(): VM {
    return ViewModelProvider(this).get(VM::class.java)
}

inline fun <reified VM : ViewModel> Fragment.sharedViewModel(): VM {
    return ViewModelProvider(requireActivity()).get(VM::class.java)
}