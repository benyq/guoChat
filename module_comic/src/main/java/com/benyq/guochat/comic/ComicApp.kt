package com.benyq.guochat.comic

import android.app.Application
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.benyq.guochat.comic.local.ComicObjectBox
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.IModuleInit
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

/**
 * @author benyq
 * @time 2021/2/14
 * @e-mail 1520063035@qq.com
 * @note comic 独立时的 Application
 */
@HiltAndroidApp
class ComicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}

class ComicInit : IModuleInit {
    override fun onInitAhead(application: Application) {
        ComicObjectBox.init(application)
    }
}