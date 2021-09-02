package com.benyq.guochat.wanandroid

import android.app.Application
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.IModuleInit
import dagger.hilt.android.HiltAndroidApp

/**
 *
 * @author benyq
 * @date 2021/8/20
 * @email 1520063035@qq.com
 */
@HiltAndroidApp
class WanAndroidApp : Application() {

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}

class WanAndroidInit: IModuleInit {

    companion object {
        lateinit var instance: Application
    }

    override fun onInitAhead(application: Application) {
        instance = application
    }
}