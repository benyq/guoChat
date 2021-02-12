package com.benyq.guochat.ui.openeye

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.benyq.guochat.R
import com.benyq.module_base.ext.getViewModel
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.guochat.model.vm.OpenEyeRecommendViewModel
import com.benyq.module_base.ui.base.LifecycleFragment
import com.benyq.guochat.ui.common.openeye.WaterDropHeader
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.getScreenWidth
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.footer.BallPulseFooter
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recommend.*
import kotlinx.android.synthetic.main.fragment_recommend.refreshLayout


/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class RecommendFragment : LifecycleFragment<OpenEyeRecommendViewModel>(){

    override fun initVM(): OpenEyeRecommendViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_recommend

    /**
     * 列表左or右间距
     */
    private var bothSideSpace : Int = 0
    /**
     * 列表中间内间距，左or右。
     */
    private var middleSpace :Int = 0

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
        rvRecommend.adapter = mAdapter
        rvRecommend.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        rvRecommend.itemAnimator = null
        rvRecommend.setHasFixedSize(true)
        rvRecommend.addItemDecoration(ItemDecoration())

        refreshLayout.setRefreshHeader(WaterDropHeader(mContext))
        refreshLayout.setRefreshFooter(BallPulseFooter(mContext))
        refreshLayout.setOnRefreshListener { mViewModel.onRefresh() }
        refreshLayout.setOnLoadMoreListener { mViewModel.onLoadMore() }
    }

    override fun initData() {
        super.initData()
        mViewModel.onRefresh()
    }

    override fun dataObserver() {
        with(mViewModel) {
            bannerDataResult.observe(viewLifecycleOwner, Observer { liveData ->
                bannerView.viewPager2
                bannerView.addBannerLifecycleObserver(this@RecommendFragment)
                bannerView.indicator = CircleIndicator(mContext)
                bannerView.setBannerRound(20f)
                bannerView.adapter = RecommendBannerAdapter(liveData)
                bannerView.setOnBannerListener(object: OnBannerListener<CommunityRecommend.ItemX>{
                    override fun OnBannerClick(data: CommunityRecommend.ItemX?, position: Int) {
                        if (data != null) {
                            ActionUrl.process(mContext, data.data.actionUrl)
                        }
                    }
                })
            })
            recommendDataResult.observe(viewLifecycleOwner) {
                mAdapter.setList(it)
                refreshLayout.finishRefresh(500)
            }
            recommendDataMoreResult.observe(viewLifecycleOwner) {
                mAdapter.addData(it)
                refreshLayout.closeHeaderOrFooter()
            }
            loadingType.observe(viewLifecycleOwner) {
                if (it.isLoading) {
                    refreshLayout.finishRefresh()
                    refreshLayout.finishLoadMoreWithNoMoreData()
                }
            }
        }
    }


    inner class RecommendBannerAdapter(data: List<CommunityRecommend.ItemX>) : BannerImageAdapter<CommunityRecommend.ItemX>(data) {
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

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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