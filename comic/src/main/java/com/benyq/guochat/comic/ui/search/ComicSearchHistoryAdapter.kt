package com.benyq.guochat.comic.ui.search

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.local.SearchHistoryRecord
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_search_history.view.*

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchHistoryAdapter : BaseQuickAdapter<SearchHistoryRecord, BaseViewHolder>(R.layout.comic_item_search_history){

    init {
        addChildClickViewIds(R.id.ivDelete)
    }

    override fun convert(holder: BaseViewHolder, item: SearchHistoryRecord) {
        holder.itemView.tvTitle.text = item.name
    }
}