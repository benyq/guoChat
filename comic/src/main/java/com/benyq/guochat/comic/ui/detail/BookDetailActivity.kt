package com.benyq.guochat.comic.ui.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.databinding.ComicActivityBookDetailBinding
import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.model.bean.Comic
import com.benyq.guochat.comic.model.vm.BookDetailViewModel
import com.benyq.guochat.comic.ui.home.GridItemDecoration
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.*
import com.benyq.module_base.ui.base.LifecycleActivity
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note 漫画详情页
 */
@AndroidEntryPoint
class BookDetailActivity : LifecycleActivity<BookDetailViewModel, ComicActivityBookDetailBinding>() {

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

    override fun isSupportSwipeBack() = true

    override fun provideViewBinding() = ComicActivityBookDetailBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun initView() {
        ImmersionBar.setTitleBar(this, binding.acToolbar)
        binding.acAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val totalScrollRange = appBarLayout.totalScrollRange
            val percent = 1 - (abs(verticalOffset).toFloat() / totalScrollRange)
            binding.clLayout.alpha = percent
        })

        binding.rvBookChapter.layoutManager = GridLayoutManager(this, 2)
        binding.rvBookChapter.adapter = mChapterAdapter
        val space = dip2px(5).toInt()
        binding.rvBookChapter.addItemDecoration(GridItemDecoration(space, space, space))
        mChapterAdapter.setOnItemClickListener { _, _, position ->
            if (!mChapterAdapter.data[position].isRead) {
                mChapterAdapter.data[position].isRead = true
                mChapterAdapter.notifyItemChanged(position)
            }
            viewModelGet().updateBookShelf(mBookComic.comic_id, position, mChapterAdapter.data.size)
            gotoRead(mComicId, position)
        }

        binding.headView.setBackAction { finish() }
        binding.headView.setMenuAction {
            //加入书架
            if (this@BookDetailActivity::mBookComic.isInitialized) {
                if (mBookShelfTable == null) {
                    viewModelGet().addBookToShelf(mBookComic.comic_id, mBookComic.name, mBookComic.cover, mChapterAdapter.data.size)
                }else {
                    viewModelGet().removeBookFromShelf(mBookComic.comic_id)
                }
            }
        }
        binding.btnPreview.setOnClickListener {
            if (mBookShelfTable != null) {
                gotoRead(mBookShelfTable!!.comicId, mBookShelfTable!!.readChapterPosition)
            }else {
                binding.btnPreview.text = "继续: ${mChapterAdapter.data[0].name}"
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
                    binding.tvBookName.text = comic.name
                    binding.tvAuthor.text = comic.author.name
                    binding.tvDesContent.text = comic.description
                    binding.ivCover.loadImage(comic.cover)
                    binding.ivBg.loadImage(comic.wideCover ?: "")
                    mChapterAdapter.setList(chapterList)

                    viewModelGet().searchBookShelf(mComicId)
                }
            }

            bookShelfResult.observe(this@BookDetailActivity) {
                mBookShelfTable = it
                binding.btnPreview.text = "继续: ${mChapterAdapter.data[it.readChapterPosition].name}"
                binding.headView.setMenuSrc(R.drawable.comic_ic_collected)
            }

            addOrRemoveResult.observe(this@BookDetailActivity) {
                if (it) {
                    binding.headView.setMenuSrc(R.drawable.comic_ic_collected)
                }else {
                    binding.headView.setMenuSrc(R.drawable.comic_ic_collect)
                    mBookShelfTable = null
                    binding.btnPreview.setText(R.string.comic_read_immediately)
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
                    binding.btnPreview.text = "继续: ${mChapterAdapter.data[backPosition].name}"
                }
            }
        }, enterAnim = 0)
    }
}