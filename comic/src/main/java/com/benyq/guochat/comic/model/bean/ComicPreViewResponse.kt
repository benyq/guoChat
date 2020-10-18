package com.benyq.guochat.comic.model.bean

import com.google.gson.annotations.SerializedName

/**
 * @author benyq
 * @time 2020/10/6
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ComicPreViewResponse(
    val status: Int,
    @SerializedName("chapter_id")
    val chapterId: String,
    val type: Int,
    val image_list: List<ImageListBean>
)

data class ImageListBean(
    val height: Int = 0,
    val imHeightArr: List<String>,
    @SerializedName("image_id")
    val imageId: String,
    val images: List<Image>,
    val img05: String,
    val img50: String,
    val location: String,
    @SerializedName("total_tucao")
    val totalTucao: String,
    val type: String,
    val webp: String,
    var currentIndex: Int = 0,
    val width: Int = 0
)

data class Image(
    val height: Int = 0,
    val id: String,
    val img05: String,
    val img50: String,
    val sort: String,
    val width: Int = 0
)