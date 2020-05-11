package com.benyq.mvvm.mvvm

import android.app.Activity
import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.benyq.mvvm.R
import com.benyq.mvvm.ext.toast

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
interface IMvmView {

    /**
     * 如果要处理Throwable的各种情况，可以重写该方法
     */
    fun showError(t: Throwable) {
        realToast(R.string.unkown_error)
        hideLoading()
    }

    fun showToast(msg: String) {
        realToast(msg)
        hideLoading()
    }

    fun showToast(@StringRes strRes: Int) {
        realToast(strRes)
        hideLoading()
    }


    fun realToast(msg: String) {
        if (this is Fragment) {
            this.toast(msg)
        } else if (this is Activity) {
            this.toast(msg)
        }
    }

    fun realToast(@StringRes strRes: Int) {
        if (this is Fragment) {
            this.toast(strRes)
        } else if (this is Activity) {
            this.toast(strRes)
        }
    }

    fun showEmptyView() {}

    fun showLoading(content: String?){}

    fun hideLoading() {}
}