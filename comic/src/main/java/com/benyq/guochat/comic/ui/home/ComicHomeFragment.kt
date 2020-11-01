package com.benyq.guochat.comic.ui.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.ComicHomeViewModel
import com.benyq.guochat.comic.ui.search.ComicSearchBookFragment
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_fragment_home.*

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicHomeFragment : LifecycleFragment<ComicHomeViewModel>(){

    private val mAdapter by lazy { ComicHomeAdapter(requireActivity()) }

    override fun getLayoutId() = R.layout.comic_fragment_home


    override fun initVM(): ComicHomeViewModel  = getViewModel()

    override fun initView() {
        rvComicList.layoutManager = LinearLayoutManager(mContext)
        rvComicList.adapter = mAdapter
        tvSearchBook.setOnClickListener {
            ComicSearchBookFragment.switchFragment(requireActivity())
        }
        ivBack.setOnClickListener {
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