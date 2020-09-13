package com.benyq.guochat.ui.openeye

import com.benyq.guochat.R
import com.benyq.guochat.model.bean.openeye.Daily
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/13
 * @e-mail 1520063035@qq.com
 * @note
 */
//class DailyPaperAdapter : BaseQuickAdapter<Daily.Item, BaseViewHolder>(R.layout.item_open_eye_daily_pager){
class DailyPaperAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_open_eye_daily_pager){

    override fun convert(holder: BaseViewHolder, item: String) {

    }
}