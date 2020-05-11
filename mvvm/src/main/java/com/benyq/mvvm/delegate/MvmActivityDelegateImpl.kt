package com.benyq.mvvm.delegate

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.benyq.mvvm.ViewModelFactory
import com.benyq.mvvm.annotation.BindViewModel
import com.benyq.mvvm.mvvm.IMvmActivity
import java.lang.reflect.Field

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class MvmActivityDelegateImpl(private val activity: Activity) : ActivityDelegateImpl(activity), IMvmActivity {

    private val iMvmActivity = activity as IMvmActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        initViewModel()
        super.onCreate(savedInstanceState)
        iMvmActivity.dataObserver()
    }

    override fun getLayoutId() = 0

    private fun initViewModel() {
        activity.javaClass.fields
            .filter { it.isAnnotationPresent(BindViewModel::class.java) }
            .forEach {
                it?.apply {
                    isAccessible = true
                    set(activity, getViewModel(this))
                }
            }
    }

    private fun getViewModel(field: Field): ViewModel {
        return ViewModelFactory.createViewModel(iMvmActivity, field)
    }
}