package com.benyq.guochat.comic.ui.detail

import android.widget.FrameLayout
import android.widget.TextView
import com.benyq.guochat.comic.R
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.ext.dip2px
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class ComicDetailTagAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.comic_item_comic_detail_tag) {

    private val bgColor = arrayOf("#4687d7", "#f0983c", "#a67fc0", "#c099cc00")

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvTag, item)

        holder.itemView.context.run {
            holder.getView<FrameLayout>(R.id.flParent).background = DrawableBuilder(this)
                .corner(dip2px(3))
                .fill(bgColor.random())
                .build()
        }
    }
}