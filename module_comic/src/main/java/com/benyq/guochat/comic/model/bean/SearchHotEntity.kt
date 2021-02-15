package com.benyq.guochat.comic.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @author benyq
 * @time 2020/9/26
 * @e-mail 1520063035@qq.com
 * @note
 */
data class SearchHotEntity(val defaultSearch: String, val hotItems: List<HotItemsBean>)

data class HotItemsBean(
    @SerializedName("comic_id")
    val comicId: String,
    val name: String,
    val bgColor: String,
)