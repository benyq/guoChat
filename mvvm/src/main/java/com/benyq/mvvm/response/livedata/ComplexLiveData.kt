package com.benyq.mvvm.response.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note 同时包含 正确与错误情况
 */
open class ComplexLiveData<T, E> {

    private val successLiveData by lazy { MutableLiveData<T>() }

    private val errorLiveData by lazy { MutableLiveData<E>() }

    open fun postSuccessValue(value: T?) {
        successLiveData.postValue(value)
    }

    open fun postErrorValue(value: E?) {
        errorLiveData.postValue(value)
    }

    open fun observeNonNull(
        owner: LifecycleOwner,
        successBlock: (T) -> Unit,
        errorBlock: ((E) -> Unit)? = null
    ) {
        this.successLiveData.observe(owner, Observer { successBlock(it) })

        this.errorLiveData.observe(owner, Observer { errorBlock?.invoke(it) })
    }

    open fun observe(
        owner: LifecycleOwner,
        successBlock: (T?) -> Unit,
        errorBlock: ((E?) -> Unit)? = null
    ) {
        this.successLiveData.observe(owner, Observer { successBlock(it) })

        this.errorLiveData.observe(owner, Observer { errorBlock?.invoke(it) })
    }
}
