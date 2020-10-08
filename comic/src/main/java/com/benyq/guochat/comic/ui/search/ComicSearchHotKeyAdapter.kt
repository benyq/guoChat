package com.benyq.guochat.comic.ui.search

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.HotItemsBean
import com.benyq.mvvm.DrawableBuilder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_search_hot_key.view.*

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchHotKeyAdapter :
    BaseQuickAdapter<HotItemsBean, BaseViewHolder>(R.layout.comic_item_search_hot_key) {

    override fun convert(holder: BaseViewHolder, item: HotItemsBean) {
        val tvHotKey = holder.itemView.tvHotKey
        tvHotKey.text = item.name
        if (tvHotKey.background != null && tvHotKey.background is GradientDrawable) {
            (tvHotKey.background as GradientDrawable).setColor(Color.parseColor(item.bgColor))
        } else {
            tvHotKey.background = DrawableBuilder(context)
                .corner(10f)
                .fill(item.bgColor)
                .build()
        }
    }
}