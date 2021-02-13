package com.benyq.guochat.comic.ui.search

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.ComicSearchResponse
import com.benyq.module_base.ext.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

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
        holder.getView<TextView>(R.id.tvBookName).text = item.name
        holder.getView<TextView>(R.id.tvAuthor).text = item.author
        holder.getView<TextView>(R.id.tvDesContent).text = item.description
        holder.getView<TextView>(R.id.tvChapter).text = "共:${item.passChapterNum}"
        holder.getView<ImageView>(R.id.ivBookCover).loadImage(item.cover, round = 0)
    }
}