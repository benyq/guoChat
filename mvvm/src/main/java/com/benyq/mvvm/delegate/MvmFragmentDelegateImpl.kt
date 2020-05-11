package com.benyq.mvvm.delegate

import android.os.Bundle
import android.view.View
import androidx.collection.LruCache
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.benyq.mvvm.ViewModelFactory
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.IMvmFragment
import java.lang.reflect.Field

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class MvmFragmentDelegateImpl(private val fm: FragmentManager, private val fragment: Fragment) :
    FragmentDelegateImpl(fm, fragment), IMvmFragment {

    override fun getLayoutId(): Int = 0

    private val iMvmFragment = fragment as IMvmFragment

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        initViewModel()
        super.onViewCreated(v, savedInstanceState)

        iMvmFragment.dataObserver()
    }

    /**
     *  根据 @BindViewModel 注解, 查找注解标示的变量（ViewModel）
     *  并且 创建 ViewModel 实例, 注入到变量中
     */
    private fun initViewModel() {
        fragment.javaClass.fields
            .filter { it.isAnnotationPresent(BindViewModel::class.java) }
            .forEach {
                it?.apply {
                    isAccessible = true
                    set(fragment, getViewModel(this))
                }
            }
    }


    private fun getViewModel(field: Field): ViewModel {
        return ViewModelFactory.createViewModel(fragment, field)
    }
}