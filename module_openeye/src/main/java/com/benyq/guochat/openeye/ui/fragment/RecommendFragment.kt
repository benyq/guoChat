package com.benyq.guochat.openeye.ui.fragment

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.benyq.guochat.openeye.ActionUrl
import com.benyq.guochat.openeye.ui.adapter.RecommendAdapter
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.getScreenWidth
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleFragment
import com.benyq.module_base.ui.waterdrop.WaterDropHeader
import com.benyq.guochat.openeye.databinding.FragmentRecommendBinding
import com.benyq.guochat.openeye.model.bean.CommunityRecommend
import com.benyq.guochat.openeye.model.vm.OpenEyeRecommendViewModel
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class RecommendFragment : LifecycleFragment<OpenEyeRecommendViewModel, FragmentRecommendBinding>() {

    override fun initVM(): OpenEyeRecommendViewModel = getViewModel()

    override fun provideViewBinding() = FragmentRecommendBinding.inflate(layoutInflater)

    /**
     * 列表左or右间距
     */
    private var bothSideSpace: Int = 0

    /**
     * 列表中间内间距，左or右。
     */
    private var middleSpace: Int = 0

    private var maxImageWidth = 0

    private lateinit var mAdapter: RecommendAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        bothSideSpace = context.dip2px(14).toInt()
        middleSpace = context.dip2px(3).toInt()
        maxImageWidth = (context.getScreenWidth() - bothSideSpace * 2 - middleSpace) / 2
    }


    override fun initView() {
        super.initView()
        mAdapter = RecommendAdapter(this, maxImageWidth)
        binding.rvRecommend.apply {
            adapter = mAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            itemAnimator = null
            setHasFixedSize(true)
            addItemDecoration(ItemDecoration())
        }

        binding.refreshLayout.apply {
            setRefreshHeader(WaterDropHeader(mContext))
            setRefreshFooter(BallPulseFooter(mContext))
            setOnRefreshListener { mViewModel.onRefresh() }
            setOnLoadMoreListener { mViewModel.onLoadMore() }
        }
    }

    override fun initData() {
        super.initData()
        mViewModel.onRefresh()
    }

    override fun dataObserver() {
        with(mViewModel) {
            bannerDataResult.observe(viewLifecycleOwner, { liveData ->
                binding.bannerView.apply {
                    addBannerLifecycleObserver(this@RecommendFragment)
                    indicator = CircleIndicator(mContext)
                    setBannerRound(20f)
                    setAdapter(RecommendBannerAdapter(liveData))
                    setOnBannerListener(object : OnBannerListener<CommunityRecommend.ItemX> {
                        override fun OnBannerClick(data: CommunityRecommend.ItemX?, position: Int) {
                            if (data != null) {
                                ActionUrl.process(
                                    mContext,
                                    data.data.actionUrl
                                )
                            }
                        }
                    })
                }
            })
            recommendDataResult.observe(viewLifecycleOwner) {
                mAdapter.setList(it)
                binding.refreshLayout.finishRefresh(500)
            }
            recommendDataMoreResult.observe(viewLifecycleOwner) {
                mAdapter.addData(it)
                binding.refreshLayout.closeHeaderOrFooter()
            }
            loadingType.observe(viewLifecycleOwner) {
                if (it.isLoading) {
                    binding.refreshLayout.finishRefresh()
                    binding.refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }


    inner class RecommendBannerAdapter(data: List<CommunityRecommend.ItemX>) :
        BannerImageAdapter<CommunityRecommend.ItemX>(data) {
        override fun onBindView(
            holder: BannerImageHolder?,
            data: CommunityRecommend.ItemX,
            position: Int,
            size: Int
        ) {
            holder?.imageView?.run {
                Glide.with(this).load(data.data.image).into(this)
            }
        }
    }

    /**
     * 社区整个垂直列表的间隙
     */
    inner class ItemDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

            outRect.top = bothSideSpace

            when (spanIndex) {
                0 -> {
                    outRect.left = bothSideSpace
                    outRect.right = middleSpace
                }
                else -> {
                    outRect.left = middleSpace
                    outRect.right = bothSideSpace
                }
            }
        }
    }
}