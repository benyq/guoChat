package com.benyq.guochat.chat.app

import android.app.Application
import android.util.Log
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.media.NotificationHelper
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.IModuleInit
import com.benyq.module_base.ui.base.BaseApplication
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.mvvm.ErrorHandler
import com.benyq.module_base.mvvm.ExceptionReason
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
class ChatApp : BaseApplication(){

    companion object {
        lateinit var sInstance: ChatApp
    }


    override fun onCreate() {
        super.onCreate()
        sInstance = this
        CommonModuleInit.onInit(this)
    }
}

class ChatInit: IModuleInit {
    override fun onInitAhead(application: Application) {
        ChatObjectBox.init(application)
        Pinyin.init(null)
        NotificationHelper.init(application)
        injectError()
    }


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
            } else if(e.message == "Job was cancelled"){
                //协程取消异常，一般在网络请求取消时出现
            }else {
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