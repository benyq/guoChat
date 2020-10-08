package com.benyq.guochat.comic.ui.detail

import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.vm.BookDetailViewModel
import com.benyq.guochat.comic.ui.home.GridItemDecoration
import com.benyq.guochat.comic.ui.home.loadImage
import com.benyq.mvvm.ext.dip2px
import com.benyq.mvvm.ext.getViewModel
import com.benyq.mvvm.ext.goToActivity
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.ui.base.LifecycleActivity
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.comic_activity_book_detail.*
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note 漫画详情页
 */
@AndroidEntryPoint
class BookDetailActivity : LifecycleActivity<BookDetailViewModel>() {

    private lateinit var mComicId: String

    private val mChapterAdapter: BookChapterAdapter = BookChapterAdapter()

    override fun initVM(): BookDetailViewModel = getViewModel()

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .titleBar(R.id.acToolbar)
            .autoDarkModeEnable(true, 0.2f)
            .init()
    }

    override fun getLayoutId() = R.layout.comic_activity_book_detail

    override fun initView() {
        ImmersionBar.setTitleBar(this, acToolbar)
        acAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percent = 1 - (abs(verticalOffset).toFloat() / totalScrollRange)
            clLayout.alpha = percent
        })

        rvBookChapter.layoutManager = GridLayoutManager(this, 2)
        rvBookChapter.adapter = mChapterAdapter
        val space = dip2px(5).toInt()
        rvBookChapter.addItemDecoration(GridItemDecoration(space, space, space))
        mChapterAdapter.setOnItemClickListener { adapter, view, position ->
            if (!mChapterAdapter.data[position].isRead) {
                mChapterAdapter.data[position].isRead = true
                mChapterAdapter.notifyItemChanged(position)
            }
            goToActivity<ReadComicBookActivity>(
                ComicIntentExtra.chapterPosition to position,
                ComicIntentExtra.chapterList to mChapterAdapter.data,
                enterAnim = 0
            )
        }

        headView.setBackAction { finish() }
        headView.setMenuAction {
            //加入书架

        }
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            bookDetailResult.observe(this@BookDetailActivity) { result ->
                result.isSuccess?.run {
                    loge(comic)
                    tvBookName.text = comic.name
                    tvAuthor.text = comic.author.name
                    tvDesContent.text = comic.description
                    ivCover.loadImage(comic.cover)
                    ivBg.loadImage(comic.wideCover ?: "")
                    mChapterAdapter.setList(chapterList)
                }
            }
        }
    }

    override fun initData() {
        mComicId = intent.getStringExtra(ComicIntentExtra.comicId) ?: ""
        viewModelGet().getComicDetail(mComicId)
    }
}