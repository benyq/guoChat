package com.benyq.guochat.comic.ui.shelf

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.local.BookShelfTable
import com.benyq.guochat.comic.ui.home.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_home_head_list.view.*

/**
 * @author benyq
 * @time 2020/9/22
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicShelfAdapter : BaseQuickAdapter<BookShelfTable, BaseViewHolder>(R.layout.comic_item_book_shelf){
    override fun convert(holder: BaseViewHolder, item: BookShelfTable) {
        holder.itemView.ivBookCover.loadImage(item.coverUrl)
        holder.itemView.tvBookName.text = item.comicName
    }
}