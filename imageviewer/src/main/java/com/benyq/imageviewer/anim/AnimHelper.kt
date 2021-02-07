package com.benyq.imageviewer.anim

import android.app.Activity
import androidx.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.lifecycle.LifecycleOwner
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.OnAnimatorListener
import com.benyq.imageviewer.onDestroy
import com.benyq.mvvm.ext.getScreenHeight
import com.benyq.mvvm.ext.loge
import com.gyf.immersionbar.ImmersionBar

/**
 * @author benyqYe
 * date 2021/1/28
 * e-mail 1520063035@qq.com
 * description 控制 ImageView动画
 */

internal object AnimHelper {

    /**
     * 1. 先在屏幕原位置放一个大小一样的ImageView，然后进行缩放
     * 2.同时背景颜色也要从透明改为改透明
     */
    fun startEnterAnim(
        parentView: View,
        fullView: View,
        thumbnailView: View?,
        duration: Long,
        listener: OnAnimatorListener, anim: Boolean = true
    ) {
        if (thumbnailView == null || !anim) {
            return
        }
        changeFullViewSizeByThumbnailView(fullView, thumbnailView, parentView)

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
        fullView.postDelayed(50) {
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

    fun startExitAnim(
        owner: LifecycleOwner, parentView: View, fullView: View,
        thumbnailView: View?, duration: Long, fraction: Float, listener: OnAnimatorListener
    ) {
        fullView.post {
            TransitionManager.beginDelayedTransition(fullView.parent as ViewGroup,
                listener.getTransition().apply {
                    this.duration = duration
                    addListener(object:TransitionListenerAdapter(){
                        override fun onTransitionEnd(transition: Transition) {
                            listener.onExitAnimEnd()
                        }
                    })
                }
            )
            fullView.scaleX = 1f
            fullView.scaleY = 1f
            fullView.translationX = 0f
            fullView.translationY = 0f

            changeFullViewSizeByThumbnailView(fullView, thumbnailView, parentView)

            if (fullView is ImageView && thumbnailView is ImageView) {
                fullView.scaleType = thumbnailView.scaleType
            }
            owner.onDestroy {
                TransitionManager.endTransitions(fullView.parent as ViewGroup)
            }
        }
        AnimBgHelper.onExit(parentView, fraction, duration)
    }

    private fun changeFullViewSizeByThumbnailView(fullView: View, thumbnailView: View?, parentView: View) {

        if (thumbnailView != null) {
            fullView.layoutParams = fullView.layoutParams.apply {
                //宽高
                width = thumbnailView.width
                height = thumbnailView.height
                //位置
                val location = getLocationOnScreen(thumbnailView)
                val parentLocation = getParentLocationOnScreen(parentView, Components.isFullScreen)
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
                                ConstraintSet.START, location[0] - parentLocation[1]
                            )
                        }
                        constraintSet.applyTo(fullView.parent as ConstraintLayout)
                    }
                    else -> {

                        if (this is ViewGroup.MarginLayoutParams) {
                            this.marginStart = location[0]
                            this.topMargin = location[1] - parentLocation[1]
                        }
                    }
                }
            }
        }else {
            //这是退出时会走的
            fullView.scaleX = 0f
            fullView.scaleY = 0f
        }
    }



    private fun getLocationOnScreen(thumbnailView: View): IntArray {
        val location = IntArray(2)
        thumbnailView.getLocationOnScreen(location)
        return location
    }


    /**
     * @param fullScreen 因为这时候 parenView可能还没显示，location.top == 0, 所以需要状态栏填充
     */
    private fun getParentLocationOnScreen(parentView: View, fullScreen: Boolean): IntArray {
        val location = IntArray(2)
        parentView.getLocationOnScreen(location)
        if (!fullScreen && !parentView.isShown) {
            location[1] = ImmersionBar.getStatusBarHeight(parentView.context as Activity)
        }
        return location
    }
}