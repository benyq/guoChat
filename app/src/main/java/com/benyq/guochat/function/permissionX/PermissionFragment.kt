package com.benyq.guochat.function.permissionX

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.benyq.mvvm.ext.beforeM
import com.benyq.mvvm.ext.fromM
import com.benyq.mvvm.ext.fromN

/**
 * @author benyq
 * @time 2020/4/22
 * @e-mail 1520063035@qq.com
 * @note
 */

typealias PermissionCallBack = (Boolean, List<String>)->Unit

class PermissionFragment : Fragment(){

    private var callBack : PermissionCallBack? = null
    private var mContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun requestNow(cb: PermissionCallBack, vararg permissions: String) {
        callBack = cb
        if (mContext != null && mContext is FragmentActivity) {
            if (beforeM()) {
                callBack?.invoke(true, mutableListOf())
            }else {
                val denyPermissions = mutableListOf<String>()
                permissions.forEach {
                    if (mContext!!.checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED) {
                        denyPermissions.add(it)
                    }
                }
                if (denyPermissions.isEmpty()) {
                    callBack?.invoke(true, mutableListOf())
                }else {
                    requestPermissions(denyPermissions.toTypedArray(), 1)
                }
            }
        }else {
            requestPermissions(permissions, 1)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            val denyList = mutableListOf<String>()
            grantResults.forEachIndexed { index, i ->
                if (i != PackageManager.PERMISSION_GRANTED) {
                    denyList.add(permissions[index])
                }
            }
            callBack?.invoke(denyList.isEmpty(), denyList)
        }
    }
}