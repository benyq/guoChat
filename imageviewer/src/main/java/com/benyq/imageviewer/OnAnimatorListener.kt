package com.benyq.imageviewer

import androidx.transition.Transition
import android.view.View


internal interface OnAnimatorListener {

    fun getTransition(): Transition

    //主要是为 图片与视频 设置不同效果
    fun onEnterTransition(fullView: View, thumbnailView: View)

    fun onExitAnimEnd()

    fun beforeExitAnim(){}
}