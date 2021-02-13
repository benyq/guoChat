package com.benyq.guochat.comic.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.databinding.ComicFragmentHomeBinding
import com.benyq.guochat.comic.model.vm.ComicHomeViewModel
import com.benyq.guochat.comic.ui.search.ComicSearchBookFragment
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicHomeFragment : LifecycleFragment<ComicHomeViewModel, ComicFragmentHomeBinding>(){

    private val mAdapter by lazy { ComicHomeAdapter(requireActivity()) }

    override fun provideViewBinding() = ComicFragmentHomeBinding.inflate(layoutInflater)

    override fun initVM(): ComicHomeViewModel  = getViewModel()

    override fun initView() {
        binding.rvComicList.layoutManager = LinearLayoutManager(mContext)
        binding.rvComicList.adapter = mAdapter
        binding.tvSearchBook.setOnClickListener {
            ComicSearchBookFragment.switchFragment(requireActivity())
        }
        binding.ivBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun dataObserver() {
        with(mViewModel){
            comicList.observe(viewLifecycleOwner, {
                mAdapter.setList(it)
            })
        }
    }

    override fun initData() {
        mViewModel.boutiqueList()
    }
}