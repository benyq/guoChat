package com.benyq.guochat.comic.ui.detail

import android.graphics.Color
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.Chapter
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.getColorRef
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_book_chapter.view.*

/**
 * @author benyq
 * @time 2020/10/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class BookChapterAdapter :
    BaseQuickAdapter<Chapter, BaseViewHolder>(R.layout.comic_item_book_chapter) {

    override fun convert(holder: BaseViewHolder, item: Chapter) {

        holder.itemView.tvChapterName?.run {
            if (item.type == "3") {
                holder.itemView.isEnabled = false
                text = context.getString(R.string.comic_no_support_vip_tip)
                setTextColor(context.getColorRef(R.color.comic_color_3333_80))
            } else {
                holder.itemView.isEnabled = true
                text = item.name
                setTextColor(if (item.isRead) context.getColorRef(R.color.comic_color_3333_80) else context.getColorRef(R.color.comic_color_333333))
            }
        }

    }
}