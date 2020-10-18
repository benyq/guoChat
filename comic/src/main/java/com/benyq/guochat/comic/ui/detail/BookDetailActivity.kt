package com.benyq.guochat.comic.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.model.bean.Comic
import com.benyq.guochat.comic.model.vm.BookDetailViewModel
import com.benyq.guochat.comic.ui.home.GridItemDecoration
import com.benyq.guochat.comic.ui.home.loadImage
import com.benyq.mvvm.SmartJump
import com.benyq.mvvm.ext.*
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

    private var mBookShelfTable: BookShelfTable? = null
    private lateinit var mBookComic: Comic

    override fun initVM(): BookDetailViewModel = getViewModel()

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .autoDarkModeEnable(true, 0.2f)
            .init()
    }

    override fun getLayoutId() = R.layout.comic_activity_book_detail

    @SuppressLint("SetTextI18n")
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
        mChapterAdapter.setOnItemClickListener { _, _, position ->
            if (!mChapterAdapter.data[position].isRead) {
                mChapterAdapter.data[position].isRead = true
                mChapterAdapter.notifyItemChanged(position)
            }
            viewModelGet().updateBookShelf(mBookComic.comic_id, position, mChapterAdapter.data.size)
            gotoRead(mComicId, position)
        }

        headView.setBackAction { finish() }
        headView.setMenuAction {
            //加入书架
            if (this@BookDetailActivity::mBookComic.isInitialized) {
                if (mBookShelfTable == null) {
                    viewModelGet().addBookToShelf(mBookComic.comic_id, mBookComic.name, mBookComic.cover, mChapterAdapter.data.size)
                }else {
                    viewModelGet().removeBookFromShelf(mBookComic.comic_id)
                }
            }
        }
        btnPreview.setOnClickListener {
            if (mBookShelfTable != null) {
                gotoRead(mBookShelfTable!!.comicId, mBookShelfTable!!.readChapterPosition)
            }else {
                btnPreview.text = "继续: ${mChapterAdapter.data[0].name}"
                gotoRead(mComicId, 0)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun dataObserver() {
        with(viewModelGet()) {
            bookDetailResult.observe(this@BookDetailActivity) { result ->
                result.isSuccess?.run {
                    loge(comic)
                    mBookComic = comic
                    tvBookName.text = comic.name
                    tvAuthor.text = comic.author.name
                    tvDesContent.text = comic.description
                    ivCover.loadImage(comic.cover)
                    ivBg.loadImage(comic.wideCover ?: "")
                    mChapterAdapter.setList(chapterList)

                    viewModelGet().searchBookShelf(mComicId)
                }
            }

            bookShelfResult.observe(this@BookDetailActivity) {
                mBookShelfTable = it
                btnPreview.text = "继续: ${mChapterAdapter.data[it.readChapterPosition].name}"
                headView.setMenuSrc(R.drawable.comic_ic_collected)
            }

            addOrRemoveResult.observe(this@BookDetailActivity) {
                if (it) {
                    headView.setMenuSrc(R.drawable.comic_ic_collected)
                }else {
                    headView.setMenuSrc(R.drawable.comic_ic_collect)
                    mBookShelfTable = null
                    btnPreview.setText(R.string.comic_read_immediately)
                }
            }
        }
    }

    override fun initData() {
        mComicId = intent.getStringExtra(ComicIntentExtra.comicId) ?: ""
        viewModelGet().getComicDetail(mComicId)
    }

    @SuppressLint("SetTextI18n")
    private fun gotoRead(comicId: String, position: Int) {
        val intent = Intent(this, ReadComicBookActivity::class.java).apply {
            putExtra(ComicIntentExtra.chapterPosition, position)
            putExtra(ComicIntentExtra.comicId, comicId)
        }
        SmartJump.from(this).startForResult(intent, { code, data ->
            if (code == Activity.RESULT_OK && data != null) {
                val backComicId = data.getStringExtra(ComicIntentExtra.comicId)
                val backPosition = data.getIntExtra(ComicIntentExtra.chapterPosition, -1)
                if (backComicId != null && backPosition != -1) {
                    btnPreview.text = "继续: ${mChapterAdapter.data[backPosition].name}"
                }
            }
        }, enterAnim = 0)
    }
}