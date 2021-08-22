package com.benyq.guochat.chat.app

import android.app.Application
import android.util.Log
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
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
//@HiltAndroidApp
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
        ChatObjectBox.init()
        Pinyin.init(null)
        NotificationHelper.init(application)
        EmojiCompat.init(BundledEmojiCompatConfig(application))
    }
}