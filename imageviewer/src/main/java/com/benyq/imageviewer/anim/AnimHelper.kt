package com.benyq.imageviewer.anim

import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.postDelayed
import androidx.transition.*
import com.benyq.imageviewer.Components
import com.benyq.mvvm.ext.loge
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image.*

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
    fun startEnterAnim(parentView: View, fullView: View, thumbnailView: View, duration: Long) {
        loge("wo俩了吗  startEnterAnim")

        fullView.layoutParams = fullView.layoutParams.apply {

            //宽高
            width = thumbnailView.width
            height = thumbnailView.height

            //位置
            val location = getLocationOnScreen(thumbnailView)

            when (parentView) {
                is ConstraintLayout -> {
                    loge("startEnterAnim ConstraintLayout")
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

//            if (fullView is ImageView && thumbnailView is ImageView) {
//                fullView.scaleType = thumbnailView.scaleType
//            }
        }

        AnimBgHelper.onEnter(parentView, 0f, duration)

        //开始动画
        startTransition(fullView, thumbnailView, duration)
    }


    fun startTransition(fullView: View, thumbnailView: View, duration: Long) {
        if (fullView is ImageView) {
            loge("wo俩了吗")
            fullView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
        fullView.postDelayed(1000L) {
//            TransitionManager.beginDelayedTransition(
//                fullView.parent as ViewGroup,
//                TransitionSet().apply {
//                    addTransition(ChangeBounds())
//                    addTransition(ChangeImageTransform())
//                    addTransition(ChangeTransform())
//                    this.duration = duration
//                    interpolator = DecelerateInterpolator()
//                }
//            )

            if (fullView is ImageView) {
                loge("wo俩了吗")
                fullView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
            fullView.layoutParams = fullView.layoutParams.apply {
//                width = ViewGroup.LayoutParams.MATCH_PARENT
//                height = ViewGroup.LayoutParams.MATCH_PARENT
                if (this is ViewGroup.MarginLayoutParams) {
                    marginStart = 0
                    topMargin = 0
                }
            }

        }
    }

    private fun getLocationOnScreen(thumbnailView: View): IntArray {
        val location = IntArray(2)
        thumbnailView.getLocationOnScreen(location)
        return location
    }
}