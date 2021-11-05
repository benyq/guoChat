package com.benyq.guochat.chat.app

import android.app.Application
import android.util.Log
import androidx.emoji.bundled.BundledEmojiCompatConfig
import androidx.emoji.text.EmojiCompat
import com.benyq.guochat.chat.local.ChatObjectBox
import com.benyq.guochat.media.NotificationHelper
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.IModuleInit
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.BaseApplication
import com.github.promeg.pinyinhelper.Pinyin
import dagger.hilt.android.HiltAndroidApp

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

class ChatInit : IModuleInit {
    override fun onInitAhead(application: Application) {
        ChatObjectBox.init()
        Pinyin.init(null)
        NotificationHelper.init(application)
        EmojiCompat.init(BundledEmojiCompatConfig(application))
    }
}