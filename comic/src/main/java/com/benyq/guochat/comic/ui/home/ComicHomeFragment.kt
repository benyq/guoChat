package com.benyq.guochat.comic.ui

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.ComicHomeViewModel
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicHomeFragment : LifecycleFragment<ComicHomeViewModel>(){

    override fun getLayoutId() = R.layout.comic_fragment_home


    override fun initVM(): ComicHomeViewModel  = getViewModel()

    override fun initView() {

    }

    override fun dataObserver() {

    }

}