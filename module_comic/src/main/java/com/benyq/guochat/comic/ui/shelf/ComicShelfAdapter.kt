package com.benyq.guochat.comic.ui.shelf

import android.widget.ImageView
import android.widget.TextView
import com.benyq.guochat.comic.R
import com.benyq.guochat.database.entity.comic.BookShelfTable
import com.benyq.module_base.ext.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/22
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicShelfAdapter : BaseQuickAdapter<BookShelfTable, BaseViewHolder>(R.layout.comic_item_book_shelf){
    override fun convert(holder: BaseViewHolder, item: BookShelfTable) {
        holder.getView<ImageView>(R.id.ivBookCover).loadImage(item.coverUrl)
        holder.getView<TextView>(R.id.tvBookName).text = item.comicName
    }
}