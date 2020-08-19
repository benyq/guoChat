package com.benyq.guochat.app

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.benyq.guochat.function.other.NotificationHelper
import com.benyq.guochat.local.ObjectBox
import com.benyq.guochat.model.net.ServiceFactory
import com.github.promeg.pinyinhelper.Pinyin
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltAndroidApp
class App : Application(), ViewModelStoreOwner{

    companion object{
        lateinit var sInstance : App
    }

    private val mAppViewModelStore: ViewModelStore = ViewModelStore()

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        sInstance = this
        ObjectBox.init(this)
        Pinyin.init(null)
        NotificationHelper.init(this)
    }

    override fun getViewModelStore()= mAppViewModelStore
}