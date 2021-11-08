package com.benyq.guochat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.module_base.BuildConfig
import com.benyq.module_base.CommonModuleInit
import com.benyq.module_base.MMKVValue
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.BaseApplication
import dagger.hilt.android.HiltAndroidApp
import okhttp3.internal.addHeaderLenient

/**
 * @author benyqYe
 * date 2021/2/18
 * e-mail 1520063035@qq.com
 * description 壳app的Application
 */
@HiltAndroidApp
class App : BaseApplication(){

    override fun onCreate() {
        super.onCreate()
        CommonModuleInit.onInit(this)
    }
}