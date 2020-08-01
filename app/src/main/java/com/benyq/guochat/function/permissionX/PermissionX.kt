package com.benyq.guochat.function.permissionX

import android.content.Context
import android.util.Log
import androidx.fragment.app.FragmentActivity

/**
 * @author benyq
 * @time 2020/4/22
 * @e-mail 1520063035@qq.com
 * @note
 */
object PermissionX {
    private const val TAG = "PermissionCallBack"

    fun request(activity: Context, vararg permissions: String, callBack: PermissionCallBack) {
        if (activity is FragmentActivity) {
            val fragmentManager = activity.supportFragmentManager
            val existFragment = fragmentManager.findFragmentByTag(TAG)
            val fragment = if (existFragment != null) {
                existFragment as PermissionFragment
            }else {
                val permissionFragment = PermissionFragment()
                fragmentManager.beginTransaction().add(permissionFragment, TAG).commitNow()
                permissionFragment
            }
            fragment.requestNow(callBack, *permissions)
        }else {
            throw RuntimeException("Context must be FragmentActivity")
        }
    }

}