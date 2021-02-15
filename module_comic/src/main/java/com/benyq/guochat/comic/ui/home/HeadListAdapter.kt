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
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class HeadListAdapter(layoutId: Int) : BaseQuickAdapter<RecommendEntity.ComicLists.Comic, BaseViewHolder>(layoutId){

    override fun convert(holder: BaseViewHolder, item: RecommendEntity.ComicLists.Comic) {
        val ivBookCover: ImageView = holder.getView(R.id.ivBookCover)
        ivBookCover.loadImage(item.cover)
        val tvBookName: TextView = holder.getView(R.id.tvBookName)
        tvBookName.text = item.name
    }
}