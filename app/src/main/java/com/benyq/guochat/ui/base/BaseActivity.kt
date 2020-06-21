package com.benyq.guochat.ui.base

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.benyq.guochat.R
import com.benyq.guochat.ui.common.NormalProgressDialogManager
import com.benyq.mvvm.base.IActivity
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.loge
import com.gyf.immersionbar.ImmersionBar
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
    var isSupportSwipeBack = true

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        pendingTransition()

    }

    override fun initWidows() {
        if (isImmersionBarEnabled()) {
            initImmersionBar()
        }
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
        return swipeBackAction(ev) || super.dispatchTouchEvent(ev)
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


    override fun finish() {
        super.finish()
        pendingTransition()
    }

    open fun pendingTransition() {
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out)
    }

    open fun isImmersionBarEnabled() = true

    open fun initImmersionBar() {
        immersionBar {
            fitsSystemWindows(true)
            statusBarColor(R.color.darkgrey)
            statusBarDarkFont(true, 0.2f) //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        }
    }

    private var touchX = 0f
    private var touchY = 0f
    private var touchOriginX = 0f
    private var scrolling = false

    /**
     * 开启右移退出后，水平的方向的事件都被拦截
     */
    private fun swipeBackAction(ev: MotionEvent?): Boolean {
        if (!isSupportSwipeBack) {
            return isSupportSwipeBack
        }

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = ev.x
                touchY = ev.y
                touchOriginX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                //拦截水平方向的移动
                val moveX = -(ev.x - touchX).toInt()
                val moveY = -(ev.y - touchY).toInt()

                //0 - 0.15 * getScreenWidth() 范围内允许右滑
                val maxTouchX = 0.15 * getScreenWidth()

                if ((abs(ViewConfiguration.get(this).scaledTouchSlop) > abs(moveX) && !scrolling) || touchOriginX > maxTouchX) {
                    return false
                }
                touchX = ev.x
                touchY = ev.y
                if (abs(moveY) > abs(moveX) && !scrolling) {
                    return false
                }
                scrolling = true
                loge("MotionEvent.ACTION_MOVE   moveX $moveX  touchOriginX $touchOriginX")
                if (ev.x - touchOriginX >= 0) {
                    window.decorView.scrollBy(moveX, 0)
                } else {
                    window.decorView.scrollTo(0, 0)
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                //偏移量
                val moveX = ev.x - touchOriginX
                loge(
                    "MotionEvent.ACTION_UP   moveX $moveX  touchOriginX $touchOriginX  --- ${ViewConfiguration.get(
                        this
                    ).scaledTouchSlop}"
                )
                touchX = 0f
                touchOriginX = 0f

                if (abs(ViewConfiguration.get(this).scaledTouchSlop) < abs(moveX) && scrolling) {
                    //水平方向存在滑动
                    if (moveX > 0.4 * getScreenWidth()) {
                        //finish
                        val valueObjectAnimator = ValueAnimator.ofFloat(moveX - getScreenWidth())
                            .setDuration(200)
                        valueObjectAnimator.addUpdateListener {
                            val x = it.animatedValue as Float
                            loge("valueObjectAnimator $x")
                            window.decorView.scrollBy(x.toInt(), 0)
                        }
                        valueObjectAnimator.addListener(onEnd = {
                            finish()
                        })
                        valueObjectAnimator.start()
                    } else {
                        //rollBack
                        window.decorView.scrollTo(0, 0)
                    }
                    scrolling = false
                    return true
                } else {
                    window.decorView.scrollTo(0, 0)
                    scrolling = false
                }
            }
        }
        return false
    }
}