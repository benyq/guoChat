package com.benyq.guochat.comic.ui.home

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_home_head_list.view.*

/**
 * @author benyq
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class HeadListAdapter(layoutId: Int) : BaseQuickAdapter<RecommendEntity.ComicLists.Comic, BaseViewHolder>(layoutId){

    override fun convert(holder: BaseViewHolder, item: RecommendEntity.ComicLists.Comic) {
        holder.itemView.ivBookCover.loadImage(item.cover)
        holder.itemView.tvBookName.text = item.name
    }
}