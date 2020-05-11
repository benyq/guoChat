package com.benyq.mvvm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.benyq.mvvm.mvvm.BaseViewModel
import com.benyq.mvvm.mvvm.IMvmActivity
import com.benyq.mvvm.mvvm.IMvmFragment
import com.benyq.mvvm.mvvm.IMvmView
import com.benyq.mvvm.response.SharedData
import com.benyq.mvvm.response.SharedType
import java.lang.reflect.Field

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
object ViewModelFactory {

    /**
     *  创建 对应的 ViewModel, 并且 添加 通用 SharedData (LiveData) 到 ViewModel中
     */
    fun createViewModel(fragment: Fragment, field: Field): BaseViewModel<*> {
        val viewModel = realCreateViewModel(field, fragment)
        initSharedData(fragment as IMvmFragment, viewModel)
        return viewModel
    }

    /**
     *  创建 对应的 ViewModel, 并且 添加 通用 SharedData (LiveData) 到 ViewModel中
     */
    fun createViewModel(activity: IMvmActivity, field: Field): BaseViewModel<*> {
        val viewModel = realCreateViewModel(field, activity as FragmentActivity)
        initSharedData(activity, viewModel)
        return viewModel
    }

    @Suppress("UNCHECKED_CAST")
    private fun realCreateViewModel(field: Field, owner: ViewModelStoreOwner): BaseViewModel<*> {
        val viewModelClass = field.genericType as Class<BaseViewModel<*>>

        return ViewModelProvider(owner).get(viewModelClass)
    }

    private fun initSharedData(view: IMvmView, viewModel: BaseViewModel<*>) {

        val observer: Observer<SharedData> = Observer { sharedData ->
            sharedData?.run {
                when (type) {
                    SharedType.TOAST -> view.showToast(msg)
                    SharedType.ERROR -> view.showError(throwable)
                    SharedType.SHOW_LOADING -> {
                        view.showLoading(msg)
                    }
                    SharedType.HIDE_LOADING -> view.hideLoading()
                    SharedType.RESOURCE -> view.showToast(strRes)
                    SharedType.EMPTY -> view.showEmptyView()
                }
            }
        }

        // 订阅通用 observer
        viewModel.sharedData.observe(view as LifecycleOwner, observer)
    }
}