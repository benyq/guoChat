package com.benyq.imageviewer.fragment

import android.os.Bundle
import android.view.View
import com.benyq.imageviewer.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image.*

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示 image， 普通与长图
 */

class ImageFragment : BasePreviewFragment(){

    override fun getLayoutId() = R.layout.fragment_image

    override fun getFullViewId() = R.id.ivPreviewImg

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(mData.url).into(ivPreviewImg)
//        ivPreviewImg.run {
//            setZoomTransitionDuration(3000)
//            minimumScale = 0.4f
//            mediumScale = 1f
//            maximumScale = 1.5f
//        }
    }
}