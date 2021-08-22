package com.benyq.module_base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.alibaba.android.arouter.launcher.ARouter
import com.benyq.guochat.database.DataObjectBox
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.http.ApiException
import com.benyq.module_base.mvvm.ErrorHandler
import com.benyq.module_base.mvvm.ExceptionReason
import com.google.gson.JsonParseException
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.tencent.mmkv.MMKV
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException


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
    private const val ModuleWanAndroid = "com.benyq.guochat.wanandroid.WanAndroidInit"

    private var initModuleNames = arrayOf(ModuleComic, ModuleOpenEye, ModuleChat, ModuleWanAndroid)


    fun onInit(app: Application) {
        Toasts.init(app)
        Logger.addLogAdapter(object : AndroidLogAdapter(PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(3)
            .tag("benyq")
            .build()) {
            override fun isLoggable(priority: Int, tag: String?) = BuildConfig.DEBUG
        })

        if (BuildConfig.isDebug) {
            ARouter.openLog() // 打印日志
            ARouter.openDebug() // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(app)
        MMKV.initialize(app)
        DataObjectBox.init(app)
        BGASwipeBackHelper.init(app, null)
        injectError()
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

    private fun injectError(){
        ErrorHandler.injectHandler { e ->
            if (e is HttpException) {
                onException(ExceptionReason.BAD_NETWORK)
            } else if (e is ConnectException || e is UnknownHostException) {
                onException(ExceptionReason.CONNECT_ERROR)
            } else if (e is InterruptedIOException) {
                onException(ExceptionReason.CONNECT_TIMEOUT)
            } else if (e is JsonParseException
                || e is JSONException
                || e is ParseException
            ) {
                onException(ExceptionReason.PARSE_ERROR)
            } else if (e is ApiException) {
                //假设 code == 0 时是登陆异常，需要重新登录
                if (e.code == -101) {
                    Log.e("benyq", e.msg)
                }
                Toasts.show(e.msg)
            } else if(e.message == "Job was cancelled"){
                //协程取消异常，一般在网络请求取消时出现
            }else {
                onException(ExceptionReason.UNKNOWN_ERROR)
            }
        }
    }

    /**
     * 请求异常
     */
    private fun onException(reason: ExceptionReason) {
        when (reason) {
            ExceptionReason.CONNECT_ERROR -> Toasts.show("连接错误")
            ExceptionReason.CONNECT_TIMEOUT -> Toasts.show("连接超时")
            ExceptionReason.BAD_NETWORK -> Toasts.show("网络错误")
            ExceptionReason.PARSE_ERROR -> Toasts.show("解析错误")
            ExceptionReason.UNKNOWN_ERROR -> Toasts.show("未知错误")
            else -> Toasts.show("未知错误")
        }
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
