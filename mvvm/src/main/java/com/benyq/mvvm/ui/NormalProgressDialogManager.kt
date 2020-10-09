package com.benyq.mvvm.ui

import android.app.Activity
import android.content.Context

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/2/4
 * description:
 */
object NormalProgressDialogManager {

    private var mDialog: BenyqProgressDialog? = null

    @Synchronized
    fun showLoading(context: Context, content: String?, cancelable: Boolean = false) {
        mDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        if (context !is Activity) {
            return
        }
        mDialog = BenyqProgressDialog(context, content).apply {
            setCancelable(cancelable)
            if (!isShowing && !context.isFinishing) {
                show()
            }
        }

    }

    fun dismissLoading() {
        mDialog?.dismiss()
        mDialog = null
    }

}