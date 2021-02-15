package com.benyq.guochat.comic.ui.detail

import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.Chapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/10/13
 * @e-mail 1520063035@qq.com
 * @note 阅读界面左边目录
 */
class LeftBookChapterAdapter : BaseQuickAdapter<Chapter, BaseViewHolder>(R.layout.comic_item_left_chapter){
    override fun convert(holder: BaseViewHolder, item: Chapter) {
    }
}