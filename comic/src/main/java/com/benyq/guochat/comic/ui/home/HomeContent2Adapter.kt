package com.benyq.guochat.comic.ui.home

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_home_content2.view.*

/**
 * @author benyq
 * @time 2020/9/24
 * @e-mail 1520063035@qq.com
 * @note
 */
class HomeContent2Adapter : BaseQuickAdapter<RecommendEntity.ComicLists.Comic, BaseViewHolder>(R.layout.comic_item_home_content2){

    override fun convert(holder: BaseViewHolder, item: RecommendEntity.ComicLists.Comic) {
        holder.itemView.ivBookCover.loadImage(item.cover, 10)
        holder.itemView.tvTitle.text = item.name
        holder.itemView.tvAuthor.text = item.author_name
        holder.itemView.tvDes.text = item.description
        holder.itemView.tvSubTitle.text = item.subTitle

    }
}