package com.benyq.mvvm.mvvm

import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benyq.mvvm.Setting
import com.benyq.mvvm.ext.getClass
import com.benyq.mvvm.ext.showLog
import com.benyq.mvvm.ext.tryCatch
import com.benyq.mvvm.response.BenyqResponse
import com.benyq.mvvm.response.SharedData
import com.benyq.mvvm.response.SharedType
import com.socks.library.KLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseViewModel<T : BaseRepository> : ViewModel() , IMvmView{

    val sharedData by lazy { MutableLiveData<SharedData>() }

    // 通过反射注入 repository
    val mRepository: T by lazy { getClass<T>(this).newInstance() }

    fun launchUI(block: suspend CoroutineScope.() -> Unit, error: ((Throwable) -> Unit)? = null, finalBlock: (() -> Unit)? = null) : Job{
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


    override fun showError(t: Throwable) {
        sharedData.value = SharedData(throwable = t, type = SharedType.ERROR)
    }

    override fun showToast(msg: String) {
        sharedData.value = SharedData(msg, type = SharedType.TOAST)
    }

    override fun showToast(@StringRes strRes: Int) {
        sharedData.value = SharedData(strRes = strRes, type = SharedType.RESOURCE)
    }

    override fun showEmptyView() {
        sharedData.value = SharedData(type = SharedType.EMPTY)
    }

    override fun showLoading(content: String?) {
        sharedData.value = SharedData(msg = content ?: "", type = SharedType.SHOW_LOADING)
    }

    override fun hideLoading() {
        sharedData.value = SharedData(type = SharedType.HIDE_LOADING)
    }

    fun <R> quickLaunch(block: Execute<R>.() -> Unit) {
        Execute<R>().apply(block)
    }

    inner class Execute<R> {

        private var startBlock: (() -> Unit)? = null
        private var finalBlock: (() -> Unit)? = null

        private var successBlock: ((R?) -> Unit)? = null
        private var successRspBlock: ((BenyqResponse<R>) -> Unit)? = null

        private var failBlock: ((String?) -> Unit) = { showToast(it ?: Setting.MESSAGE_EMPTY) }
        private var exceptionBlock: ((Throwable) -> Unit)? = null

        fun onStart(block: () -> Unit) {
            this.startBlock = block
        }

        fun request(block: suspend CoroutineScope.() -> BenyqResponse<R>?) {

            startBlock?.invoke()

            launchUI({

                successBlock?.let {
                    block()?.execute(successBlock, failBlock)
                } ?: block()?.executeRsp(successRspBlock, failBlock)

            }, exceptionBlock, finalBlock)
        }

        fun onSuccess(block: (R?) -> Unit) {
            this.successBlock = block
        }

        fun onSuccessRsp(block: (BenyqResponse<R>) -> Unit) {
            this.successRspBlock = block
        }

        fun onFail(block: (String?) -> Unit) {
            this.failBlock = block
        }

        fun onException(block: (Throwable) -> Unit) {
            this.exceptionBlock = block
        }

        fun onFinal(block: () -> Unit) {
            this.finalBlock = block
        }
    }


}