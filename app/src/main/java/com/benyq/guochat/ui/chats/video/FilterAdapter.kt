package com.benyq.guochat.ui.chats.video

import android.graphics.Color
import android.widget.TextView
import com.benyq.guochat.R
import com.benyq.guochat.function.video.filter.FilterType
import com.benyq.guochat.model.bean.VideoFilter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyqYe
 * date 2021/1/25
 * e-mail 1520063035@qq.com
 * description filter adapter
 */

class FilterAdapter : BaseQuickAdapter<VideoFilter, BaseViewHolder>(R.layout.item_video_filter){

    override fun convert(holder: BaseViewHolder, item: VideoFilter) {
        val tvFilterName = holder.getView<TextView>(R.id.tvFilterName)
        tvFilterName.text = item.name
        if (item.checked) {
            tvFilterName.setTextColor(Color.WHITE)
            tvFilterName.setBackgroundResource(R.drawable.shape_filter_selected)
        }else {
            tvFilterName.setTextColor(Color.BLACK)
            tvFilterName.setBackgroundResource(R.color.white)
        }
    }

    fun selectFilter(position: Int) {
        data.forEachIndexed { index, videoFilter ->
            videoFilter.checked = index == position
        }
        notifyDataSetChanged()
    }
}