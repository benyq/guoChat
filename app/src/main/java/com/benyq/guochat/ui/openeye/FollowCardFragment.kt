package com.benyq.guochat.ui.openeye

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.guochat.*
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.guochat.ui.base.BaseFragment
import com.benyq.mvvm.ext.gone
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ext.visible
import com.github.chrisbanes.photoview.PhotoView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ICacheManager
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.android.synthetic.main.fragment_open_eye_follow_card.*
import java.io.File


/**
 * @author benyq
 * @time 2020/9/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class FollowCardFragment : BaseFragment(){

    companion object {
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommendItem"
    }

    private var mCommunityRecommendItem: CommunityRecommend.Item? = null
    private var videoPlayer: StandardGSYVideoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommunityRecommendItem = arguments?.getParcelable(EXTRA_RECOMMEND_ITEM_JSON)
    }

    override fun getLayoutId() = R.layout.fragment_open_eye_follow_card

    override fun initView() {
        super.initView()
        ivPullDown.setOnClickListener { requireActivity().finish() }
        mCommunityRecommendItem?.run {
            ivAvatar.loadImage(data.content.data.owner.avatar)
            tvNickName.text = data.content.data.owner.nickname
            tvCollectionCount.text = data.content.data.consumption.collectionCount.toString()
            tvReplyCount.text = data.content.data.consumption.replyCount.toString()
            tvDescription.text = data.content.data.description
            if (data.content.data.description.isBlank()) tvDescription.gone() else tvDescription.visible()
            data.content.data.tags?.first()?.run {
                tvTagName.text = name
            } ?: tvTagName.gone()

            if (data.content.type == "video") {
                //增加封面
                val imageView = ImageView(mContext)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.loadImage(data.content.data.cover.detail, round = 0)

                //设置 viewPlayer
                videoPlayer = StandardGSYVideoPlayer(mContext)
                videoPlayer?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                flContainer.addView(videoPlayer, 0)
                videoPlayer?.run {
                    thumbImageView = imageView
                    backButton.gone()
                    this.visible()
                    isLooping = true
                    setVideoAllCallBack(object : GSYSampleCallBack() {
                        override fun onClickBlank(url: String?, vararg objects: Any?) {
                            super.onClickSeekbar(url, *objects)
                            switchHeaderAndUgcInfoVisibility()
                        }

                    })
                    setIsTouchWiget(false)
                    setUp(data.content.data.playUrl, true, null)
                }
            }

            if (data.content.type == "ugcPicture") {
                data.content.data.urls?.run {
                    viewPagerPhotos.visible()
                    viewPagerPhotos.overScrollNever()
                    viewPagerPhotos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    viewPagerPhotos.offscreenPageLimit = 1
                    viewPagerPhotos.adapter = PhotosAdapter(data.content.data.urls)
                    if (data.content.data.urls.size > 1) {
                        tvPhotoCount.visible()
                        tvPhotoCount.text = String.format(getString(R.string.photo_count), 1, data.content.data.urls.size)
                    } else {
                        tvPhotoCount.gone()
                    }
                    viewPagerPhotos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            tvPhotoCount.text = String.format(getString(R.string.photo_count), position + 1, data.content.data.urls.size)
                        }
                    })
                }
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                //释放所有
                videoPlayer?.setVideoAllCallBack(null)
                requireActivity().finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        videoPlayer?.onVideoResume()
    }

    override fun onPause() {
        super.onPause()
        videoPlayer?.onVideoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }

    private fun switchHeaderAndUgcInfoVisibility(){
        //显示以及隐藏
        if (ivPullDown.visibility == View.VISIBLE) {
            ivPullDown.invisibleAlphaAnimation()
            llUgcInfo.invisibleAlphaAnimation()
        }else {
            ivPullDown.visibleAlphaAnimation()
            llUgcInfo.visibleAlphaAnimation()
        }
    }

    inner class PhotosAdapter(private val dataList: List<String>): RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {

        inner class ViewHolder(view: PhotoView) : RecyclerView.ViewHolder(view) {
            val photoView = view
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val photoView = PhotoView(parent.context)
            photoView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return ViewHolder(photoView)
        }

        override fun getItemCount() = dataList.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.photoView.loadImage(dataList[position], round = 0)
            holder.photoView.setOnClickListener {
                switchHeaderAndUgcInfoVisibility()
            }
        }
    }
}