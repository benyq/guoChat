package com.benyq.guochat.ui.discover

import com.benyq.guochat.R
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.chrisbanes.photoview.PhotoView

/**
 * @author benyq
 * @time 2020/6/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class CirclePhotoAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_circle_photo){

    override fun convert(holder: BaseViewHolder, item: String) {
        Glide.with(context).load(item).into(holder.getView<PhotoView>(R.id.ivCirclePhoto))
    }
}