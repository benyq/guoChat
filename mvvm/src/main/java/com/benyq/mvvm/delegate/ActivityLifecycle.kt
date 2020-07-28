package com.benyq.mvvm.delegate

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.collection.LruCache
import androidx.fragment.app.FragmentActivity
import com.benyq.mvvm.ActivityManager
import com.benyq.mvvm.Setting
import com.benyq.mvvm.base.IActivity

/**
 * @author benyq
 * @time 2020/4/8
 * @e-mail 1520063035@qq.com
 * @note
 */
object ActivityLifecycle : Application.ActivityLifecycleCallbacks{

    private val cache by lazy { LruCache<String, ActivityDelegate>(Setting.ACTIVITY_CACHE_SIZE) }

    override fun onActivityPaused(activity: Activity) {
        fetchActivityDelegate(activity)?.onPause()
    }

    override fun onActivityStarted(activity: Activity) {
        fetchActivityDelegate(activity)?.onStart()
    }

    override fun onActivityDestroyed(activity: Activity) {
        fetchActivityDelegate(activity)?.onDestroy()
        ActivityManager.remove(activity)
        cache.remove(getKey(activity))
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        fetchActivityDelegate(activity)?.onSaveInstanceState(activity, outState)
    }

    override fun onActivityStopped(activity: Activity) {
        fetchActivityDelegate(activity)?.onStop()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        ActivityManager.add(activity)
        if (activity !is IActivity) {
            return
        }

        val activityDelegate = fetchActivityDelegate(activity)
            ?: newDelegate(activity).apply { cache.put(getKey(activity), this) }

        activityDelegate.onCreate(savedInstanceState)

        registerFragmentCallback(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        fetchActivityDelegate(activity)?.onResume()
    }

    private fun fetchActivityDelegate(activity: Activity?): ActivityDelegate? {

        if (activity !is IActivity) {
            return null
        }

        return cache.get(getKey(activity))
    }

    private fun newDelegate(activity: Activity): ActivityDelegate {

        return ActivityDelegateImpl(activity)
    }

    private fun getKey(activity: Activity): String = activity.javaClass.name + activity.hashCode()

    private fun registerFragmentCallback(activity: Activity?) {

        if (activity !is FragmentActivity) return

        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycle, true)
    }
}