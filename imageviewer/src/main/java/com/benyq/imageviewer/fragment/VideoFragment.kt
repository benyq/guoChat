package com.benyq.imageviewer.fragment

import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.core.view.postDelayed
import androidx.transition.*
import com.benyq.imageviewer.Components
import com.benyq.imageviewer.R
import com.benyq.imageviewer.anim.AnimBgHelper
import com.benyq.imageviewer.anim.AnimHelper
import com.benyq.imageviewer.widgets.video.ExoVideoView
import com.benyq.imageviewer.widgets.video.ExoVideoView2
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.visible
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ui.PlayerControlView
import kotlin.math.min

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 显示短视频
 */

internal class VideoFragment : BasePreviewFragment() {

    private lateinit var mVideoView: ExoVideoView2
    private lateinit var mIvPreview: ImageView
    private var onRendered : Boolean = false
    private var fakeScale = 1f

    override fun getLayoutId() = R.layout.fragment_video

    override fun getFullViewId(): Int {
        return if (!onRendered || fakeScale == 1f) {
            R.id.ivPreview
        } else {
            R.id.previewVideoView
        }
    }

    override fun beforeEnterAnim(fullView: View, thumbnailView: View?) {
        mVideoView = fragmentView.findViewById(R.id.previewVideoView)
        mIvPreview = fragmentView.findViewById(R.id.ivPreview)
        Glide.with(mIvPreview).load(mData.url).into(mIvPreview)

        if (thumbnailView is ImageView) {
            mIvPreview.scaleType = thumbnailView.scaleType
        }

        mVideoView.setVideoRenderedCallback(object : ExoVideoView.VideoRenderedListener {
            override fun onRendered(view: ExoVideoView) {
                onRendered = true
                mIvPreview.gone()
            }
        })

        mVideoView.addListener(object : ExoVideoView2.Listener {
            override fun onDrag(view: ExoVideoView2, fraction: Float) {
                AnimBgHelper.onDrag(view.parent as View, 1f - fraction)
            }

            override fun onRestore(view: ExoVideoView2, fraction: Float) {
                AnimBgHelper.onEnter(view.parent as View, 1f - fraction, 200)
            }

            override fun onRelease(view: ExoVideoView2, fraction: Float) {
                mViewModel.isExiting = true
                fakeScale = 1 - min(0.4f, fraction)
                beforeExitAnim()
                AnimHelper.startExitAnim(
                    viewLifecycleOwner, fragmentView,
                    mIvPreview,
                    Components.getView(mPosition),
                    300, 1f - fraction,
                    this@VideoFragment
                )
            }
        })

        mVideoView.prepare(mData.url)
    }

    override fun beforeExitAnim() {
        mIvPreview.visible()
        mIvPreview.translationX = mVideoView.translationX
        mIvPreview.translationY = mVideoView.translationY
        mVideoView.gone()

        mIvPreview.scaleX = fakeScale
        mIvPreview.scaleY = fakeScale
    }

    override fun onResume() {
        super.onResume()
        mVideoView.postDelayed(300) {
            mVideoView.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        mVideoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mVideoView.release()
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
        mIvPreview.scaleType = ImageView.ScaleType.FIT_CENTER
    }

}