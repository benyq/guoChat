package com.benyq.module_base.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.module_base.ext.tryCatch
import com.benyq.module_base.http.BenyqResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseViewModel : ViewModel() {

    val loadingType = MutableLiveData<UiState<String>>()

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
        loadingType.value = UiState(isSuccess = msg, isLoading = true)
    }

    protected fun hideLoading(){
        loadingType.value = UiState()
    }

    private fun showError(t: Throwable) {
        loadingType.value = UiState(isError = t)
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

        fun requestFlow(block: () -> Flow<BenyqResponse<R>>) {
            viewModelScope.launch(Dispatchers.Main) {
                block().onStart {
                    startBlock?.invoke()
                }.catch { t->
                    errorBlock?.invoke(t)
                }.onCompletion {
                    finalBlock?.invoke()
                }.collect {
                    it.execute(successBlock, errorBlock)
                }
            }
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
        val isError: Throwable?= null
    )
}