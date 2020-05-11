package com.benyq.guochat.app

import android.app.Application
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.local.ObjectBox
import com.github.promeg.pinyinhelper.Pinyin
import com.tencent.mmkv.MMKV

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
class App : Application(){

    companion object{
        lateinit var sInstance : App
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
//        ServiceFactory.initClient()
        sInstance = this
        ObjectBox.init(this)
        Pinyin.init(null)
        NotificationHelper.init(this)
    }


}