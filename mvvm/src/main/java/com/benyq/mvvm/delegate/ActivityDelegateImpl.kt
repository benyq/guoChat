package com.benyq.mvvm.delegate

import android.app.Activity
import android.os.Bundle
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.base.IActivity

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note  activity 代理实现类
 */
open class ActivityDelegateImpl(private val activity: Activity) : ActivityDelegate {

    private val iActivity = activity as IActivity

    // 因为在Activity中，ViewModel 需要在OnCreate之后，所以这里会有问题
    //Application.ActivityLifecycleCallbacks 在Activity类下执行，而ViewModel在子类中，会有冲突
    override fun onCreate(savedInstanceState: Bundle?) {
        // 在界面未初始化之前调用的初始化窗口
        iActivity.initWidows()

        if (iActivity.initArgs(activity.intent.extras)) {
            activity.setContentView(iActivity.getLayoutId())
            iActivity.initBefore()
            iActivity.initView()
            iActivity.initListener()
        } else {
            activity.finish()
        }
    }

    override fun onStart() {
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onStop() {
    }

    override fun onSaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onDestroy() {
    }
}