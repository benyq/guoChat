package com.benyq.mvvm.ui.base

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
import com.benyq.mvvm.R
import com.benyq.mvvm.ui.NormalProgressDialogManager
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar


/**
 * @author benyq
 * @time 2020/4/19
 * @e-mail 1520063035@qq.com
 * @note 所有Activity基类
 */
abstract class BaseActivity : AppCompatActivity(), IActivity, BGASwipeBackHelper.Delegate {

    /**
     * 有些场景可能不需要隐藏输入法
     */
    var hideKeyboard = true

    /**
     * 是否有ViewModel
     */
    var isLifeCircle = false

    private lateinit var appViewModelProvider: ViewModelProvider

    protected lateinit var mSwipeBackHelper: BGASwipeBackHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        initSwipeBackFinish()
        super.onCreate(savedInstanceState)
        initWidows()
        if (getLayoutId() == 0) {
            setContentView(getLayoutView())
        }else {
            setContentView(getLayoutId())
        }
        initBefore()
        initView()
        initListener()
        if (!isLifeCircle) {
            initData()
        }
    }

    open fun getAppViewModelProvider(): ViewModelProvider {
        if (!this::appViewModelProvider.isInitialized) {
            appViewModelProvider = ViewModelProvider(
                applicationContext as BaseApplication,
                ViewModelProvider.AndroidViewModelFactory.getInstance(
                    application
                )
            )
        }
        return appViewModelProvider
    }

    override fun initWidows() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
    }

    open fun initData() {}
    open fun getLayoutView(): View?  = null

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
            if (isHideBar()) {
                hideBar(BarHide.FLAG_HIDE_BAR)
            }else {
                fitsSystemWindows(true)
                statusBarColor(R.color.darkgrey)
                statusBarDarkFont(
                    true,
                    0.2f
                ) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
            }
        }
    }

    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(isSupportSwipeBack)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(isOnlyTrackingLeftEdge())
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
    }

    override fun isSupportSwipeBack() = false

    open fun isOnlyTrackingLeftEdge() = false

    //是否隐藏状态栏与导航栏
    open fun isHideBar() = false

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    override fun onSwipeBackLayoutCancel() {

    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {

    }

    override fun onBackPressed() {
        if (isSupportSwipeBack) {
            // 正在滑动返回的时候取消返回按钮事件
            if (mSwipeBackHelper.isSliding) {
                return
            }
            mSwipeBackHelper.backward()
        }else {
            super.onBackPressed()
        }
    }

    open fun isSupportViewBinding() = false
}