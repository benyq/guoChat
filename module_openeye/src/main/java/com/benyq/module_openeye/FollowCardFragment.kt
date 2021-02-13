package com.benyq.module_openeye

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.benyq.module_base.ext.*
import com.benyq.module_base.ui.base.BaseFragment
import com.benyq.module_openeye.bean.CommunityRecommend
import com.benyq.module_openeye.databinding.FragmentOpenEyeFollowCardBinding
import com.github.chrisbanes.photoview.PhotoView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author benyq
 * @time 2020/9/10
 * @e-mail 1520063035@qq.com
 * @note
 */
class FollowCardFragment : BaseFragment<FragmentOpenEyeFollowCardBinding>(){

    companion object {
        const val EXTRA_RECOMMEND_ITEM_JSON = "recommendItem"
    }

    private var mCommunityRecommendItem: CommunityRecommend.Item? = null
    private var videoPlayer: StandardGSYVideoPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCommunityRecommendItem = arguments?.getParcelable(EXTRA_RECOMMEND_ITEM_JSON)
    }

    override fun initView() {
        super.initView()
        binding.ivPullDown.setOnClickListener { requireActivity().finish() }
        mCommunityRecommendItem?.run {
            binding.ivAvatar.loadImage(data.content.data.owner.avatar)
            binding.tvNickName.text = data.content.data.owner.nickname
            binding.tvCollectionCount.text = data.content.data.consumption.collectionCount.toString()
            binding.tvReplyCount.text = data.content.data.consumption.replyCount.toString()
            binding.tvDescription.text = data.content.data.description
            if (data.content.data.description.isBlank()) binding.tvDescription.gone() else binding.tvDescription.visible()
            data.content.data.tags?.first()?.run {
                binding.tvTagName.text = name
            } ?: binding.tvTagName.gone()

            if (data.content.type == "video") {
                //增加封面
                val imageView = ImageView(mContext)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.loadImage(data.content.data.cover.detail, round = 0)

                //设置 viewPlayer
                videoPlayer = StandardGSYVideoPlayer(mContext)
                videoPlayer?.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                binding.flContainer.addView(videoPlayer, 0)
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
                    binding.viewPagerPhotos.visible()
                    binding.viewPagerPhotos.overScrollNever()
                    binding.viewPagerPhotos.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                    binding.viewPagerPhotos.offscreenPageLimit = 1
                    binding.viewPagerPhotos.adapter = PhotosAdapter(data.content.data.urls)
                    if (data.content.data.urls.size > 1) {
                        binding.tvPhotoCount.visible()
                        binding.tvPhotoCount.text = String.format(getString(R.string.photo_count), 1, data.content.data.urls.size)
                    } else {
                        binding.tvPhotoCount.gone()
                    }
                    binding.viewPagerPhotos.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            super.onPageSelected(position)
                            binding.tvPhotoCount.text = String.format(getString(R.string.photo_count), position + 1, data.content.data.urls.size)
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
        if (binding.ivPullDown.visibility == View.VISIBLE) {
            binding.ivPullDown.invisibleAlphaAnimation()
            binding.llUgcInfo.invisibleAlphaAnimation()
        }else {
            binding.ivPullDown.visibleAlphaAnimation()
            binding.llUgcInfo.visibleAlphaAnimation()
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

    override fun provideViewBinding() = FragmentOpenEyeFollowCardBinding.inflate(layoutInflater)
}