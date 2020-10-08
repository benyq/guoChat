package com.benyq.guochat.comic.ui

import com.benyq.guochat.comic.ComicConfig
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.BookDetailViewModel
import com.benyq.guochat.comic.ui.home.loadImage
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_activity_book_detail.*

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note 漫画详情页
 */
@AndroidEntryPoint
class BookDetailActivity : LifecycleActivity<BookDetailViewModel>() {

    private lateinit var mComicId: String

    override fun initVM(): BookDetailViewModel = getViewModel()

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .statusBarView(R.id.toolbar)
            .autoDarkModeEnable(true, 0.2f)
            .init()
    }

    override fun getLayoutId() = R.layout.comic_activity_book_detail

    override fun initView() {

    }

    override fun dataObserver() {
        with(viewModelGet()) {
            bookDetailResult.observe(this@BookDetailActivity) { result ->
                result.isSuccess?.run {
                    tvBookName.text = comic.name
                    tvAuthor.text = comic.author.name
                    tvDesContent.text = comic.description
                    ivCover.loadImage(comic.cover)
                    ivBg.loadImage(comic.wideCover)
                }
            }
        }
    }

    override fun initData() {
        mComicId = intent.getStringExtra(ComicConfig.comicId) ?: ""
        viewModelGet().getComicDetail(mComicId)
    }
}