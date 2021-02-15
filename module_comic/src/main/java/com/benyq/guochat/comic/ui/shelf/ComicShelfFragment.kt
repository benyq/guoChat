package com.benyq.guochat.comic.ui.shelf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.databinding.ComicFragmentComicShelfBinding
import com.benyq.guochat.comic.model.vm.ComicShelfViewModel
import com.benyq.guochat.comic.ui.detail.ReadComicBookActivity
import com.benyq.guochat.comic.ui.home.GridItemDecoration
import com.benyq.module_base.SmartJump
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.getViewModel
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ui.base.LifecycleFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@AndroidEntryPoint
class ComicShelfFragment : LifecycleFragment<ComicShelfViewModel, ComicFragmentComicShelfBinding>(){

    private val mAdapter = ComicShelfAdapter()

    override fun provideViewBinding() = ComicFragmentComicShelfBinding.inflate(layoutInflater)

    override fun initVM(): ComicShelfViewModel = getViewModel()

    override fun initView() {
        super.initView()

        binding.rvComicList.layoutManager = GridLayoutManager(mContext, 3)
        binding.rvComicList.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val comicBook = mAdapter.data[position]
            gotoRead(comicBook.comicId, comicBook.readChapterPosition)
        }
        val space = mContext.dip2px(8).toInt()
        binding.rvComicList.addItemDecoration(GridItemDecoration(space, space, space))

        binding.cbSortByUpdate.setOnCheckedChangeListener { _, checked ->
            sortBookShelf(checked)
        }
        binding.ivArrange.setOnClickListener {

        }

    }

    override fun initData() {
        mViewModel.getShelfBook()
    }

    @SuppressLint("SetTextI18n")
    override fun dataObserver() {
        mViewModel.comicShelfBookResult.observe(viewLifecycleOwner) {
            val data = if (binding.cbSortByUpdate.isChecked) {
                it.sortedBy { bean->
                    bean.id
                }
            }else {
                it.sortedByDescending { bean->
                    bean.readTime
                }
            }
            mAdapter.setList(data)
            binding.tvShelfTitle.text = "${getString(R.string.comic_shelf_title)}(${data.size})"
        }
    }

    private fun gotoRead(comicId: String, position: Int) {
        val intent = Intent(mContext, ReadComicBookActivity::class.java).apply {
            putExtra(ComicIntentExtra.chapterPosition, position)
            putExtra(ComicIntentExtra.comicId, comicId)
        }
        SmartJump.from(this).startForResult(intent, { code, data ->
            if (code == Activity.RESULT_OK && data != null) {
                val backComicId = data.getStringExtra(ComicIntentExtra.comicId)
                val backPosition = data.getIntExtra(ComicIntentExtra.chapterPosition, -1)
                if (backComicId != null && backPosition != -1) {
                    mAdapter.data.find {
                        it.comicId == backComicId
                    }?.readChapterPosition = backPosition
                    initData()
                }
            }
        }, enterAnim = 0)
    }

    /**
     * @param checked
     */
    private fun sortBookShelf(checked: Boolean) {
        if (checked){
            //按更新时间排序
            mAdapter.data.sortBy {
                it.id
            }
        }else {
            //阅读顺序
            mAdapter.data.sortByDescending {
                it.readTime
            }
        }
        mAdapter.notifyDataSetChanged()
    }
}