package com.benyq.mvvm.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.benyq.mvvm.base.IFragment

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
open class FragmentDelegateImpl(private val fragmentManager: FragmentManager,
                                private val fragment: Fragment
) : FragmentDelegate{

    private val iFragment = fragment as IFragment

    override fun onAttached(context: Context) {

    }

    override fun onCreated(savedInstanceState: Bundle?) {
        var clazz = fragment.javaClass.superclass
        while (clazz?.name != Fragment::class.java.name) {
            clazz = clazz?.superclass
        }
        val field = clazz.getDeclaredField("mContentLayoutId")

        field.isAccessible = true
        field.set(fragment, iFragment.getLayoutId())
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        iFragment.initBefore()
        iFragment.initView()
        iFragment.initListener()
//        iFragment.initData()
    }

    override fun onActivityCreate(savedInstanceState: Bundle?) {
    }

    override fun onStarted() {
    }

    override fun onResumed() {
    }

    override fun onPaused() {
    }

    override fun onStopped() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onViewDestroyed() {
    }

    override fun onDestroyed() {
    }

    override fun onDetached() {
    }

    override fun isAdd() = fragment.isAdded
}