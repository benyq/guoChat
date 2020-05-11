package com.benyq.mvvm

import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

/**
 * @author benyq
 * @time 2020/4/30
 * @e-mail 1520063035@qq.com
 * @note 跳转获得 ActivityResult
 */

typealias ActivityResultCallback = (resultCode: Int, data: Intent?)->Unit

class SmartJump(fragmentManager: FragmentManager) {

    companion object{
        fun from(activity: FragmentActivity): SmartJump {
            return SmartJump(activity.supportFragmentManager)
        }

        fun from(fragment: Fragment): SmartJump {
            return SmartJump(fragment.childFragmentManager)
        }

        fun with(fragmentManager: FragmentManager): SmartJump {
            return SmartJump(fragmentManager)
        }
    }

    private val TAG = BuildConfig.APPLICATION_ID + SmartJump::class.java.simpleName

    /**
     * 防止多次跳转
     */
    private var lastJumpTime: Long = 0
    /**
     * 跳转间隔
     */
    private val interval = 400

    private  var resultBridgeFragment: ResultBridgeFragment

    init {
        resultBridgeFragment = getResultBridgeFragment(fragmentManager)
    }

    fun startForResult(intent: Intent, callback: ActivityResultCallback) {
        val nowTime = System.currentTimeMillis()
        if (nowTime < lastJumpTime + interval) {
            return
        }
        lastJumpTime = nowTime
        resultBridgeFragment.startForResult(intent, callback)
    }

    fun startForResult(clazz: Class<*>, callback: ActivityResultCallback) {
        val intent = Intent(resultBridgeFragment.activity, clazz)
        startForResult(intent, callback)
    }

    private fun startActivity(intent: Intent) {
        val nowTime = System.currentTimeMillis()
        if (nowTime < lastJumpTime + interval) {
            return
        }
        lastJumpTime = nowTime
        resultBridgeFragment.startActivity(intent)
    }

    fun startActivity(clazz: Class<*>) {
        val intent = Intent(resultBridgeFragment.activity, clazz)
        startActivity(intent)
    }


    private fun getResultBridgeFragment(fragmentManager: FragmentManager): ResultBridgeFragment {
        var bridgeFragment = fragmentManager.findFragmentByTag(TAG) as ResultBridgeFragment?
        // 假如fragment 已经添加过了  就不用重复添加了
        if (bridgeFragment == null) {
            bridgeFragment = ResultBridgeFragment()
            fragmentManager
                .beginTransaction()
                .add(bridgeFragment, TAG)
                // 防止跳转时 背景出现桌面
                .hide(bridgeFragment)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }

        return bridgeFragment
    }


    class ResultBridgeFragment : Fragment() {
        private val mActivityResultCallbacks = SparseArray<ActivityResultCallback>()
        /**
         * 每次启动都会有个不同的requestCode
         * 因为某个activity可能会多个跳转回调
         */
        private var uniqueCode = 1

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // 设备旋转 数据保留
            retainInstance = true
        }


        /**
         * 防止 同时多个activity启动 造成request相同
         *
         * @param intent   intent
         * @param callback 回调
         */
        @Synchronized
        fun startForResult(intent: Intent, callback: ActivityResultCallback) {
            // 保证requestCode 每个都不同
            uniqueCode++
            mActivityResultCallbacks.put(uniqueCode, callback)
            startActivityForResult(intent, uniqueCode)
        }


        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val callback = mActivityResultCallbacks.get(requestCode)
            callback?.invoke(resultCode, data)
            mActivityResultCallbacks.remove(requestCode)
        }
    }

}