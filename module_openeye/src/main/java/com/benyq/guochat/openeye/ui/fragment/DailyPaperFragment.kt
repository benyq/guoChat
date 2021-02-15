package com.benyq.guochat.openeye.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.openeye.ui.adapter.DailyPaperAdapter
import com.benyq.guochat.openeye.databinding.FragmentDailyPaperBinding
import com.benyq.guochat.openeye.model.vm.OpenEyeDailyPaperViewModel
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/8/30
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class DailyPaperFragment : LifecycleFragment<OpenEyeDailyPaperViewModel, FragmentDailyPaperBinding>(){

    private val mAdapter by lazy { DailyPaperAdapter() }

    override fun initVM(): OpenEyeDailyPaperViewModel = getViewModel()

    override fun provideViewBinding() = FragmentDailyPaperBinding.inflate(layoutInflater)

    override fun initView() {
        super.initView()
        binding.rvDailyPaper.layoutManager = LinearLayoutManager(mContext)
        binding.rvDailyPaper.adapter = mAdapter
        mAdapter.setList(listOf("hello", "world", "你好", "世界"))

        binding.refreshLayout.run {
            setDragRate(0.7f)
            setHeaderTriggerRate(0.6f)
            setFooterTriggerRate(0.6f)
            setEnableLoadMoreWhenContentNotFull(true)
            setEnableFooterFollowWhenNoMoreData(true)
            setEnableFooterTranslationContent(true)
            setEnableScrollContentWhenLoaded(true)
            setEnableNestedScroll(true)
            setOnRefreshListener {
                requireActivity().finish()
            }
        }
    }

    override fun dataObserver() {

    }
}