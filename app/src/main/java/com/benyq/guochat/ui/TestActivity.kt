package com.benyq.guochat.ui

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import com.benyq.guochat.R
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.loge
import kotlinx.android.synthetic.main.activity_test.*
import kotlin.math.abs

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return swipeBackAction(ev) || super.dispatchTouchEvent(ev)
    }

    private var touchX = 0f
    private var touchY = 0f
    private var touchOriginX = 0f
    private var scrolling = false

    /**
     * 开启右移退出后，水平的方向的事件都被拦截
     */
    open fun swipeBackAction(ev: MotionEvent?) : Boolean{
        when(ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = ev.x
                touchY = ev.y
                touchOriginX = ev.x
            }
            MotionEvent.ACTION_MOVE -> {
                //拦截水平方向的移动
                val moveX = -(ev.x - touchX).toInt()
                val moveY = -(ev.y - touchY).toInt()
                if (abs(ViewConfiguration.get(this).scaledTouchSlop) > abs(moveX) && !scrolling) {
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
                }else {
                    window.decorView.scrollTo(0, 0)
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                //偏移量
                val moveX = ev.x - touchOriginX
                loge("MotionEvent.ACTION_UP   moveX $moveX  touchOriginX $touchOriginX  --- ${ViewConfiguration.get(this).scaledTouchSlop}")
                touchX = 0f
                touchOriginX = 0f
                scrolling = false

                if (abs(ViewConfiguration.get(this).scaledTouchSlop) < abs(moveX)) {
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
                    }else {
                        //rollBack
                        window.decorView.scrollTo(0, 0)
                    }
                    return true
                }else {
                    window.decorView.scrollTo(0, 0)
                }
            }
        }
        return false
    }
}
