package com.benyq.guochat.comic.ui.home

import android.widget.ImageView
import android.widget.TextView
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.RecommendEntity
import com.benyq.module_base.ext.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/24
 * @e-mail 1520063035@qq.com
 * @note
 */
class HomeContent2Adapter : BaseQuickAdapter<RecommendEntity.ComicLists.Comic, BaseViewHolder>(R.layout.comic_item_home_content2){

    override fun convert(holder: BaseViewHolder, item: RecommendEntity.ComicLists.Comic) {
        holder.getView<ImageView>(R.id.ivBookCover).loadImage(item.cover, 10)
        holder.getView<TextView>(R.id.tvTitle).text = item.name
        holder.getView<TextView>(R.id.tvAuthor).text = item.author_name
        holder.getView<TextView>(R.id.tvDes).text = item.description
        holder.getView<TextView>(R.id.tvSubTitle).text = item.subTitle

    }
}