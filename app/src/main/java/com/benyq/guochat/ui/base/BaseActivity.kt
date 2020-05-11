package com.benyq.guochat.ui.base

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.benyq.guochat.R
import com.benyq.guochat.ui.common.NormalProgressDialogManager
import com.benyq.mvvm.base.IActivity
import com.benyq.mvvm.ext.getColorRef
import com.luck.picture.lib.immersive.ImmersiveManage
import qiu.niorgai.StatusBarCompat


/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note 所有Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity {

    /**
     * 有些场景可能不需要隐藏输入法
     */
    var hideKeyboard = true

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun initWidows() {
        StatusBarCompat.setStatusBarColor(this, getColorRef(R.color.darkgrey))
        StatusBarCompat.changeToLightStatusBar(this)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v?.windowToken, 0)
            }
            //这是为了隐藏ChatDetailActivity中的View加的
            hideView(ev)
            return super.dispatchTouchEvent(ev)
        }
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    open fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {

        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.height
            val right = left + v.width
            return !(event.x > left && event.x < right
                    && event.y > top && event.y < bottom)
        }
        return false
    }

    open fun hideView(ev: MotionEvent) {

    }

    protected fun loadingShow(msg: String?) {
        NormalProgressDialogManager.showLoading(this, msg)
    }

    protected fun loadingHide() {
        NormalProgressDialogManager.dismissLoading()
    }

}