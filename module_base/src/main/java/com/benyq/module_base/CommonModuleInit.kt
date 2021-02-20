package com.benyq.module_base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.loge
import com.socks.library.KLog
import com.tencent.mmkv.MMKV

/**
 * @author benyqYe
 * date 2021/2/18
 * e-mail 1520063035@qq.com
 * description 初始化
 */

object CommonModuleInit {

    private const val ModuleComic = "com.benyq.guochat.comic.ComicInit"
    private const val ModuleOpenEye = "com.benyq.guochat.openeye.OpenEyeInit"
    private const val ModuleChat = "com.benyq.guochat.chat.app.ChatInit"

    private var initModuleNames = arrayOf(ModuleComic, ModuleOpenEye, ModuleChat)


    fun onInit(app: Application) {
        Toasts.init(app)
        KLog.init(BuildConfig.DEBUG, "benyq")

        if (BuildConfig.isDebug) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app)
        MMKV.initialize(app)
        BGASwipeBackHelper.init(app, null)

        register(app)
        //组件初始化
        initModuleNames.forEach {
            try {
                val clazz = Class.forName(it)
                val init = clazz.newInstance() as IModuleInit
                init.onInitAhead(app)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    private fun register(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityManager.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityManager.remove(activity)
            }
        })
    }
}

/**
 * Created by zlx on 2020/9/22 14:28
 * Email: 1170762202@qq.com
 * Description: 动态配置组件Application,有需要初始化的组件实现该接口,统一在宿主app 的Application进行初始化
 */
interface IModuleInit {
    /**
     * 需要优先初始化的
     */
    fun onInitAhead(application: Application)

}
