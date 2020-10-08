package com.benyq.guochat.comic.ui

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.ComicShelfViewModel
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
class ComicShelfFragment : LifecycleFragment<ComicShelfViewModel>(){

    override fun getLayoutId() = R.layout.comic_fragment_comic_shelf

    override fun initVM(): ComicShelfViewModel = getViewModel()

    override fun dataObserver() {

    }
}