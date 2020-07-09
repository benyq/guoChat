package com.benyq.guochat.app

import android.app.Application
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.model.net.ServiceFactory
import com.github.promeg.pinyinhelper.Pinyin
import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

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
        sInstance = this
        ObjectBox.init(this)
        Pinyin.init(null)
        NotificationHelper.init(this)
        startKoin{
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
        ServiceFactory.initClient()
    }


}