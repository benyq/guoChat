package com.benyq.imageviewer.fragment

import android.transition.Transition
import android.view.View
import android.transition.AutoTransition
import com.benyq.imageviewer.R

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示短视频
 */

class VideoFragment : BasePreviewFragment() {

    override fun getLayoutId() = R.layout.fragment_video

    override fun getFullViewId() = R.id.previewVideoView

    override fun beforeEnterAnim(fullView: View, thumbnailView: View) {

    }

    override fun getTransition(): Transition {
        return AutoTransition()
    }

    override fun onEnterTransition(fullView: View, thumbnailView: View) {

    }

}