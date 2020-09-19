package com.benyq.guochat.app

import android.app.Application
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.local.ObjectBox
import com.benyq.mvvm.ext.Toasts
import com.benyq.mvvm.http.ApiException
import com.benyq.mvvm.mvvm.ErrorHandler
import com.benyq.mvvm.mvvm.ExceptionReason
import com.github.promeg.pinyinhelper.Pinyin
import com.google.gson.JsonParseException
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltAndroidApp
class App : Application(), ViewModelStoreOwner {

    companion object {
        lateinit var sInstance: App
    }

    private val mAppViewModelStore: ViewModelStore = ViewModelStore()

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        sInstance = this
        ObjectBox.init(this)
        Pinyin.init(null)
        NotificationHelper.init(this)
        injectError()
    }

    override fun getViewModelStore() = mAppViewModelStore

    private fun injectError(){
        ErrorHandler.injectHandler { e ->
            if (e is HttpException) {
                onException(ExceptionReason.BAD_NETWORK)
            } else if (e is ConnectException || e is UnknownHostException) {
                onException(ExceptionReason.CONNECT_ERROR)
            } else if (e is InterruptedIOException) {
                onException(ExceptionReason.CONNECT_TIMEOUT)
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                onException(ExceptionReason.PARSE_ERROR)
            } else if (e is ApiException) {
                //假设 code == 0 时是登陆异常，需要重新登录
                if (e.code == -101) {
                    Log.e("benyq", e.msg)
                }
                Toasts.show(e.msg)
            } else {
                onException(ExceptionReason.UNKNOWN_ERROR)
            }
        }
    }

    /**
     * 请求异常
     */
    private fun onException(reason: ExceptionReason) {
        when (reason) {
            ExceptionReason.CONNECT_ERROR -> Toasts.show("连接错误")
            ExceptionReason.CONNECT_TIMEOUT -> Toasts.show("连接超时")
            ExceptionReason.BAD_NETWORK -> Toasts.show("网络错误")
            ExceptionReason.PARSE_ERROR -> Toasts.show("解析错误")
            ExceptionReason.UNKNOWN_ERROR -> Toasts.show("未知错误")
            else -> Toasts.show("未知错误")
        }
    }

}