package com.benyq.imageviewer.fragment

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

}