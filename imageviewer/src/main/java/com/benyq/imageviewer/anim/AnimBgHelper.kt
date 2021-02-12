package com.benyq.imageviewer.anim

import android.animation.ValueAnimator
import android.graphics.Color
import android.view.View
import androidx.core.animation.addListener
import com.benyq.imageviewer.ColorTool
import com.benyq.module_base.ext.loge

/**
 * @author benyqYe
 * date 2021/1/28
 * e-mail 1520063035@qq.com
 * description 进入时背景透明度改变。透明 -> 不透明
 */

internal object AnimBgHelper {

    /**
     * @param startValue 初始透明度，一般是 0f
     */
    fun onEnter(parent: View, startValue: Float, duration: Long) {
        val valueAnimator = ValueAnimator()
        valueAnimator.setDuration(duration)
            .setFloatValues(startValue, 1f)
        valueAnimator.addUpdateListener {
            parent.setBackgroundColor(
                ColorTool.getColorWithAlpha(Color.BLACK, (it.animatedValue as Float))
            )
        }
        valueAnimator.start()
    }

    /**
     * @param startValue 初始透明度，一般是 1f
     */
    fun onExit(parent: View, startValue: Float, duration: Long) {
        val valueAnimator = ValueAnimator()
        valueAnimator.setDuration(duration)
            .setFloatValues(startValue, 0f)
        valueAnimator.addUpdateListener {
            parent.setBackgroundColor(
                ColorTool.getColorWithAlpha(Color.BLACK, (it.animatedValue as Float))
            )
        }
        valueAnimator.start()
    }

    fun onDrag(parent: View, fraction: Float) {
        loge("onDrag fraction $fraction View $parent")
        parent.setBackgroundColor(
            ColorTool.getColorWithAlpha(Color.BLACK, fraction)
        )
    }

}