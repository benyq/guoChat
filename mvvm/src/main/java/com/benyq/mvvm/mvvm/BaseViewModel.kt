package com.benyq.mvvm.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.mvvm.ext.tryCatch
import com.benyq.mvvm.response.BenyqResponse
import com.benyq.mvvm.response.SharedData
import com.benyq.mvvm.response.SharedType
import kotlinx.coroutines.*

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseViewModel : ViewModel() {

    val mSharedData by lazy { MutableLiveData<SharedData>() }

    fun launchUI(
        block: suspend CoroutineScope.() -> Unit,
        error: ((Throwable) -> Unit)? = null,
        finalBlock: (() -> Unit)? = null
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            tryCatch({
                block()
            }, {
                error?.invoke(it) ?: showError(it)
            }, {
                finalBlock?.invoke()
            })
        }
    }

    protected fun showLoading(msg: String){
        mSharedData.value = SharedData(msg = msg, type = SharedType.SHOW_LOADING)
    }

    protected fun hideLoading(){
        mSharedData.value = SharedData(type = SharedType.HIDE_LOADING)
    }

    private fun showError(t: Throwable) {
        mSharedData.value = SharedData(throwable = t, type = SharedType.ERROR)
    }

    fun <R> quickLaunch(block: Execute<R>.() -> Unit) {
        Execute<R>().apply(block)
    }

    //在 ErrorHandler 类中注入 全局异常处理方法
    inner class Execute<R> {

        private var startBlock: (() -> Unit)? = null
        private var finalBlock: (() -> Unit)? = null

        private var successBlock: ((R?) -> Unit)? = null
        private var successRspBlock: ((BenyqResponse<R>) -> Unit)? = null

        private var errorBlock: ((Throwable) -> Unit)? = ErrorHandler.errorBlock

        fun onStart(block: () -> Unit) {
            this.startBlock = block
        }

        fun request(block: suspend CoroutineScope.() -> BenyqResponse<R>?) {

            startBlock?.invoke()

            launchUI({
                successBlock?.let {
                    block()?.execute(successBlock, errorBlock)
                } ?: block()?.executeRsp(successRspBlock, errorBlock)

            }, errorBlock, finalBlock)
        }

        fun onSuccess(block: (R?) -> Unit) {
            this.successBlock = block
        }

        fun onSuccessRsp(block: (BenyqResponse<R>) -> Unit) {
            this.successRspBlock = block
        }

        fun onError(block: (Throwable) -> Unit) {
            this.errorBlock = block
        }

        fun onFinal(block: () -> Unit) {
            this.finalBlock = block
        }
    }


    open class UiState<T>(
        val isLoading: Boolean = false,
        val isRefresh: Boolean = false,
        val isSuccess: T? = null,
        val isError: String?= null
    )
}