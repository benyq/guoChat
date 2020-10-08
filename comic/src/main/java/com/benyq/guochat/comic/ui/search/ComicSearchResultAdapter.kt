package com.benyq.guochat.comic.ui.search

import android.annotation.SuppressLint
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.ComicSearchResponse
import com.benyq.guochat.comic.ui.home.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_search_result.view.*

/**
 * @author benyq
 * @time 2020/9/27
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchResultAdapter : BaseQuickAdapter<ComicSearchResponse.ComicsBean, BaseViewHolder>(R.layout.comic_item_search_result),
    LoadMoreModule {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: ComicSearchResponse.ComicsBean) {
        holder.itemView.tvBookName.text = item.name
        holder.itemView.tvAuthor.text = item.author
        holder.itemView.tvDesContent.text = item.description
        holder.itemView.tvChapter.text = "共:${item.passChapterNum}"
        holder.itemView.ivBookCover.loadImage(item.cover, round = 0)
    }
}