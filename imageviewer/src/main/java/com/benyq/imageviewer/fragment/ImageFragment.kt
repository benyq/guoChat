package com.benyq.imageviewer.fragment

import androidx.transition.*
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.widgets.PhotoView2
import com.benyq.imageviewer.R
import com.benyq.imageviewer.anim.AnimBgHelper
import com.benyq.imageviewer.anim.AnimHelper
import com.bumptech.glide.Glide

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示 image， 普通与长图
 */

internal class ImageFragment : BasePreviewFragment() {

    override fun getLayoutId() = R.layout.fragment_image

    override fun getFullViewId() = R.id.ivPreviewImg

    override fun beforeEnterAnim(fullView: View, thumbnailView: View?) {

        Glide.with(fullView).load(Components.data[mPosition].url)
            .into(fullView as PhotoView2)

        fullView.setListener(object : PhotoView2.Listener {
            override fun onDrag(view: PhotoView2, fraction: Float) {
                AnimBgHelper.onDrag(view.parent as View, 1f - fraction)
            }

            override fun onRestore(view: PhotoView2, fraction: Float) {
                AnimBgHelper.onEnter(view.parent as View, 1f - fraction, 200)
            }

            override fun onRelease(view: PhotoView2, fraction: Float) {
                mViewModel.isExiting = true
                AnimHelper.startExitAnim(viewLifecycleOwner, fragmentView,
                    fullView,
                    Components.getView(mPosition),
                    300, 1f - fraction,
                    this@ImageFragment)
            }
        })
        fullView.setOnClickListener {
            mViewModel.setExitAnimPosition(mPosition)
            mViewModel.isExiting = true
        }
        if (thumbnailView is ImageView) {
            fullView.scaleType = thumbnailView.scaleType
            fullView.setZoomTransitionDuration(200)
            fullView.minimumScale = 0.2f
            fullView.mediumScale = 1.5f
            fullView.maximumScale = 3.0f
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