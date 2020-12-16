package com.benyq.mvvm.ui.base

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.lifecycle.ViewModelProvider
import com.benyq.mvvm.R
import com.benyq.mvvm.ext.fromP
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ui.NormalProgressDialogManager
import com.gyf.immersionbar.ktx.hideStatusBar
import com.gyf.immersionbar.ktx.immersionBar
import kotlin.math.abs


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

    /**
     * 是否有ViewModel
     */
    var isLifeCircle = false

    private lateinit var appViewModelProvider: ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWidows()
        setContentView(getLayoutId())
        initBefore()
        initView()
        initListener()
        if (!isLifeCircle) {
            initData()
        }
    }

    open fun getAppViewModelProvider(): ViewModelProvider {
        if (!this::appViewModelProvider.isInitialized) {
            appViewModelProvider = ViewModelProvider(applicationContext as BaseApplication, ViewModelProvider.AndroidViewModelFactory.getInstance(application))
        }
        return appViewModelProvider
    }

    override fun initWidows() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
        if (isFullScreen()) {
            hideSystemUI()
        }
    }

    open fun initData() {}

    override fun finish() {
        super.finish()
        //enterAnim 与 exitAnim在不同时期对应不同的Activity
        //这时候exitAnim是当前Activity
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out)
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
        }
        return super.dispatchTouchEvent(ev)
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

    protected fun showLoading(msg: String?) {
        NormalProgressDialogManager.showLoading(this, msg)
    }

    protected fun hideLoading() {
        NormalProgressDialogManager.dismissLoading()
    }


    open fun isImmersionBarEnabled() = true

    open fun initImmersionBar() {
        immersionBar {
            fitsSystemWindows(!isFullScreen())
            statusBarColor(if (isFullScreen()) R.color.black else R.color.darkgrey)
            statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    open fun isFullScreen() = false

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus && isFullScreen()) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.
    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}