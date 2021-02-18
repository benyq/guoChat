package com.benyq.guochat.openeye

import android.app.Application
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.IModuleInit
import dagger.hilt.android.HiltAndroidApp

/**
 * @author benyq
 * @time 2021/2/15
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltAndroidApp
class OpenEyeApp : Application(){

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}

class OpenEyeInit: IModuleInit {
    override fun onInitAhead(application: Application) {

    }
}