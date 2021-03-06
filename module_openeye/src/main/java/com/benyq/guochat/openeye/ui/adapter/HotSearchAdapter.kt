package com.benyq.guochat.openeye.ui.adapter

import com.benyq.guochat.openeye.R
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class HotSearchAdapter : BaseDelegateMultiAdapter<String, BaseViewHolder>(){

    private val CUSTOM_HEADER = 0
    private val HOT_SEARCH_TYPE = 100

    init {
        setMultiTypeDelegate(object: BaseMultiTypeDelegate<String>(){
            override fun getItemType(data: List<String>, position: Int): Int {
                return if (position == 0) {
                    CUSTOM_HEADER
                }else {
                    HOT_SEARCH_TYPE
                }
            }

        })
        getMultiTypeDelegate()?.run {
            addItemType(CUSTOM_HEADER, R.layout.item_eye_hot_search_header)
            addItemType(HOT_SEARCH_TYPE, R.layout.item_eye_hot_search)
        }

    }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tvKeywords, item)
    }
}