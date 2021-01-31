package com.benyq.imageviewer.fragment

import android.graphics.drawable.Drawable
import android.transition.*
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.R
import com.benyq.imageviewer.anim.AnimHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示 image， 普通与长图
 */

class ImageFragment : BasePreviewFragment() {

    override fun getLayoutId() = R.layout.fragment_image

    override fun getFullViewId() = R.id.ivPreviewImg

    override fun beforeEnterAnim(fullView: View, thumbnailView: View) {

        Glide.with(fullView).load(Components.data[mPosition].url)
            .into(fullView as ImageView)

        if (thumbnailView is ImageView) {
            fullView.scaleType = thumbnailView.scaleType
        }
    }

    override fun getTransition(): Transition {
        return TransitionSet().apply {
            addTransition(ChangeBounds())
            addTransition(ChangeImageTransform())
            addTransition(ChangeTransform())
            interpolator = DecelerateInterpolator()
        }
    }

    override fun onEnterTransition(fullView: View, thumbnailView: View) {
        if (fullView is ImageView) {
            fullView.scaleType = ImageView.ScaleType.FIT_CENTER
        }
    }
}