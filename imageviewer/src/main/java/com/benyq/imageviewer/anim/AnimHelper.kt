package com.benyq.imageviewer.anim

import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.postDelayed
import com.benyq.imageviewer.OnAnimatorListener
import com.benyq.mvvm.ext.loge

/**
 * @author benyqYe
 * date 2021/1/28
 * e-mail 1520063035@qq.com
 * description 控制 ImageView动画
 */

object AnimHelper {

    /**
     * 1. 先在屏幕原位置放一个大小一样的ImageView，然后进行缩放
     * 2.同时背景颜色也要从透明改为改透明
     */
    fun startEnterAnim(
        parentView: View,
        fullView: View,
        thumbnailView: View,
        duration: Long,
        listener: OnAnimatorListener
    ) {
        fullView.layoutParams = fullView.layoutParams.apply {

            //宽高
            width = thumbnailView.width
            height = thumbnailView.height

            //位置
            val location = getLocationOnScreen(thumbnailView)

            when (parentView) {
                is ConstraintLayout -> {
                    val constraintSet = ConstraintSet().apply {
                        clone(fullView.parent as ConstraintLayout)
                        clear(fullView.id, ConstraintSet.START)
                        clear(fullView.id, ConstraintSet.TOP)
                        clear(fullView.id, ConstraintSet.BOTTOM)
                        clear(fullView.id, ConstraintSet.RIGHT)
                        //重新建立约束
                        connect(
                            fullView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                            ConstraintSet.TOP, location[1]
                        )
                        connect(
                            fullView.id, ConstraintSet.START, ConstraintSet.PARENT_ID,
                            ConstraintSet.START, location[0]
                        )
                    }
                    constraintSet.applyTo(fullView.parent as ConstraintLayout)
                }
                else -> {

                    if (this is ViewGroup.MarginLayoutParams) {
                        this.marginStart = location[0]
                        this.topMargin = location[1]
                    }
                }
            }

        }

        AnimBgHelper.onEnter(parentView, 0f, duration)
        //开始动画
        startEnterTransition(fullView, thumbnailView, duration, listener)
    }


    private fun startEnterTransition(
        fullView: View,
        thumbnailView: View,
        duration: Long,
        listener: OnAnimatorListener
    ) {
        //加延时是因为glide还没加载好图片前，scaleType会出问题
        fullView.postDelayed(100) {
            TransitionManager.beginDelayedTransition(
                fullView.parent as ViewGroup,
                listener.getTransition().apply {
                    this.duration = duration
                }
            )

            fullView.layoutParams = fullView.layoutParams.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                height = ViewGroup.LayoutParams.MATCH_PARENT
                if (this is ViewGroup.MarginLayoutParams) {
                    marginStart = 0
                    topMargin = 0
                }
            }

            listener.onEnterTransition(fullView, thumbnailView)
        }
    }

    fun startExitAnim(parentView: View,fullView: View,
                      thumbnailView: View,duration: Long, listener: OnAnimatorListener) {

        fullView.post {
            TransitionManager.beginDelayedTransition(fullView.parent as ViewGroup,
                listener.getTransition().apply {
                    this.duration = duration
                }
            )

            fullView.layoutParams = fullView.layoutParams.apply {

                //宽高
                width = thumbnailView.width
                height = thumbnailView.height

                //位置
                val location = getLocationOnScreen(thumbnailView)

                when (parentView) {
                    is ConstraintLayout -> {
                        val constraintSet = ConstraintSet().apply {
                            clone(fullView.parent as ConstraintLayout)
                            clear(fullView.id, ConstraintSet.START)
                            clear(fullView.id, ConstraintSet.TOP)
                            clear(fullView.id, ConstraintSet.BOTTOM)
                            clear(fullView.id, ConstraintSet.RIGHT)
                            //重新建立约束
                            connect(
                                fullView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID,
                                ConstraintSet.TOP, location[1]
                            )
                            connect(
                                fullView.id, ConstraintSet.START, ConstraintSet.PARENT_ID,
                                ConstraintSet.START, location[0]
                            )
                        }
                        constraintSet.applyTo(fullView.parent as ConstraintLayout)
                    }
                    else -> {

                        if (this is ViewGroup.MarginLayoutParams) {
                            this.marginStart = location[0]
                            this.topMargin = location[1]
                        }
                    }
                }

                if (fullView is ImageView && thumbnailView is ImageView) {
                    fullView.scaleType = thumbnailView.scaleType
                }
            }

        }
        AnimBgHelper.onExit(parentView, 1f, duration)
    }


    private fun getLocationOnScreen(thumbnailView: View): IntArray {
        val location = IntArray(2)
        thumbnailView.getLocationOnScreen(location)
        return location
    }
}