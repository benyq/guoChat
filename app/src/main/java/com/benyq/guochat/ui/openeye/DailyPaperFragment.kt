package com.benyq.guochat.ui.openeye

import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.R
import com.benyq.guochat.getViewModel
import com.benyq.guochat.model.vm.OpenEyeDailyPaperViewModel
import com.benyq.mvvm.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_daily_paper.*

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class DailyPaperFragment : LifecycleFragment<OpenEyeDailyPaperViewModel>(){

    private val mAdapter by lazy { DailyPaperAdapter() }

    override fun initVM(): OpenEyeDailyPaperViewModel = getViewModel()

    override fun getLayoutId() = R.layout.fragment_daily_paper

    override fun initView() {
        super.initView()
        rvDailyPaper.layoutManager = LinearLayoutManager(mContext)
        rvDailyPaper.adapter = mAdapter
        mAdapter.setList(listOf("hello", "world", "你好", "世界"))

        refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            refreshLayout.setEnableNestedScroll(true)
            setOnRefreshListener {
                requireActivity().finish()
            }
        }
    }

    override fun dataObserver() {

    }
}