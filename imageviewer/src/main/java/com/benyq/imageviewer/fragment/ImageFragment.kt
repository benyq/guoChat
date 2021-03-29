package com.benyq.imageviewer.fragment

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.transition.*
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.R
import com.benyq.imageviewer.anim.AnimBgHelper
import com.benyq.imageviewer.anim.AnimHelper
import com.benyq.imageviewer.widgets.PhotoView2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示 image， 普通与长图
 */

internal class ImageFragment : BasePreviewFragment() {

    override fun getLayoutId() = R.layout.fragment_image

    override fun getFullViewId() = R.id.ivPreviewImg

    override fun beforeEnterAnim(fullView: View, thumbnailView: View?, parentView: View) {
        Glide.with(fullView).asBitmap().load(Components.data[mPosition].url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    bitmap: Bitmap,
                    transition: com.bumptech.glide.request.transition.Transition<in Bitmap>?
                ) {
                    (fullView as ImageView).setImageBitmap(bitmap)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }

            })

        if (thumbnailView is ImageView && fullView is PhotoView2) {

            fullView.setListener(object : PhotoView2.Listener {
                override fun onDrag(view: PhotoView2, fraction: Float) {
                    AnimBgHelper.onDrag(view.parent as View, 1f - fraction)
                }

                override fun onRestore(view: PhotoView2, fraction: Float) {
                    AnimBgHelper.onEnter(view.parent as View, 1f - fraction, 200)
                }

                override fun onRelease(view: PhotoView2, fraction: Float) {
                    mViewModel.isExiting = true
                    AnimHelper.startExitAnim(
                        viewLifecycleOwner, fragmentView,
                        fullView,
                        Components.getView(mPosition),
                        300, 1f - fraction,
                        this@ImageFragment
                    )
                }
            })

            fullView.scaleType = thumbnailView.scaleType
            fullView.setZoomTransitionDuration(200)
            fullView.minimumScale = 0.2f
            fullView.mediumScale = 1.5f
            fullView.maximumScale = 3.0f
        }

        fullView.setOnClickListener {
            mViewModel.setExitAnimPosition(mPosition)
            mViewModel.isExiting = true
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