package com.benyq.guochat.comic.ui.search

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.HotItemsBean
import com.benyq.module_base.DrawableBuilder
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicSearchHotKeyAdapter :
    BaseQuickAdapter<HotItemsBean, BaseViewHolder>(R.layout.comic_item_search_hot_key) {

    override fun convert(holder: BaseViewHolder, item: HotItemsBean) {
        val tvHotKey = holder.getView<TextView>(R.id.tvHotKey)
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