package com.benyq.guochat.comic.ui.home

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.benyq.guochat.comic.ComicIntentExtra
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.benyq.guochat.comic.ui.detail.BookDetailActivity
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loadImage
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicHomeAdapter(private val activity: Context) : BaseDelegateMultiAdapter<RecommendEntity.ComicLists, BaseViewHolder>() {

    companion object {
        private const val HEAD_LIST = 1
        private const val CONTENT_1 = 2
        private const val CONTENT_2 = 3
        private const val BANNER = 4
        private const val SINGLE = 5
    }

    init {

        setMultiTypeDelegate(object : BaseMultiTypeDelegate<RecommendEntity.ComicLists>() {
            override fun getItemType(data: List<RecommendEntity.ComicLists>, position: Int): Int {
                val comicLists = data[position]
                return when {
                    //头部
                    comicLists.comics.size > 6 -> HEAD_LIST
                    //六宫格
                    comicLists.comics.size == 6 -> CONTENT_1
                    //详细介绍 4 宫格
                    comicLists.comics.size == 4 -> CONTENT_2
                    comicLists.comics.size == 1 -> SINGLE
                    else -> BANNER
                }
            }
        })

        getMultiTypeDelegate()?.run {
            addItemType(HEAD_LIST, R.layout.comic_item_home_comic_list)
            addItemType(CONTENT_1, R.layout.comic_item_home_comic_list)
            addItemType(CONTENT_2, R.layout.comic_item_home_comic_list)
            addItemType(SINGLE, R.layout.comic_item_home_content_single)
            addItemType(BANNER, R.layout.comic_item_home_content_empty)
        }
    }

    override fun convert(holder: BaseViewHolder, item: RecommendEntity.ComicLists) {
        val ivIcon : ImageView = holder.getView(R.id.ivIcon)
        ivIcon.loadImage(item.titleIconUrl)
        val tvTitle : TextView = holder.getView(R.id.tvTitle)
        tvTitle.text = item.itemTitle

        when (holder.itemViewType) {
            HEAD_LIST -> {
                val rvComics : RecyclerView = holder.getView(R.id.rvComics)
                rvComics.run {
                    if (adapter == null || adapter !is HeadListAdapter) {
                        if (itemDecorationCount > 0) {
                            removeItemDecorationAt(0)
                        }
                        addItemDecoration(HomeSortItemDecoration(context.dip2px(10).toInt(), context.dip2px(0).toInt(), context.dip2px(0).toInt()))
                        rvComics.adapter = HeadListAdapter(R.layout.comic_item_home_head_list).apply {
                            setOnItemClickListener { adapter, view, position ->
                                activity.goToActivity<BookDetailActivity>(ComicIntentExtra.comicId to item.comics[position].comicId.toString())
                            }
                        }
                    }
                    if (layoutManager == null || layoutManager !is LinearLayoutManager) {
                        layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    }
                    (adapter as HeadListAdapter).setList(item.comics)
                }

            }
            CONTENT_1 -> {
                val rvComics : RecyclerView = holder.getView(R.id.rvComics)
                rvComics.run {
                    if (adapter == null || adapter !is HeadListAdapter) {
                        if (itemDecorationCount > 0) {
                            removeItemDecorationAt(0)
                        }
                        addItemDecoration(GridItemDecoration(context.dip2px(5).toInt(), context.dip2px(5).toInt(), context.dip2px(5).toInt()))
                        rvComics.adapter = HeadListAdapter(R.layout.comic_item_home_content1).apply {
                            setOnItemClickListener { adapter, view, position ->
                                activity.goToActivity<BookDetailActivity>(ComicIntentExtra.comicId to item.comics[position].comicId.toString())
                            }
                        }
                    }
                    if (layoutManager == null || layoutManager !is GridLayoutManager) {
                        layoutManager = GridLayoutManager(context, 3)
                    }
                    (adapter as HeadListAdapter).setList(item.comics)
                }
            }
            CONTENT_2 -> {
                val rvComics : RecyclerView = holder.getView(R.id.rvComics)
                rvComics.run {
                    if (adapter == null || adapter !is HeadListAdapter) {
                        if (itemDecorationCount > 0) {
                            removeItemDecorationAt(0)
                        }
                        addItemDecoration(GridItemDecoration(context.dip2px(5).toInt(), context.dip2px(5).toInt(), context.dip2px(5).toInt()))
                        rvComics.adapter = HomeContent2Adapter().apply {
                            setOnItemClickListener { adapter, view, position ->
                                activity.goToActivity<BookDetailActivity>(ComicIntentExtra.comicId to item.comics[position].comicId.toString())
                            }
                        }
                    }
                    if (layoutManager == null || layoutManager !is GridLayoutManager) {
                        layoutManager = GridLayoutManager(context, 2)
                    }
                    (adapter as HomeContent2Adapter).setList(item.comics)
                }

            }
            SINGLE -> {
                val ivBookCover : ImageView = holder.getView(R.id.ivBookCover)
                ivBookCover.loadImage(item.comics[0].cover)
            }
            BANNER -> {
            }
        }
    }
}
