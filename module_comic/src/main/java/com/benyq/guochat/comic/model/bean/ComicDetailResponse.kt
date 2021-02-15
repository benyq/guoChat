package com.benyq.guochat.comic.model.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author benyq
 * @time 2020/9/29
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ComicDetailResponse(
    @SerializedName("chapter_list")
    val chapterList: List<Chapter>,
    val comic: Comic)

@Parcelize
data class Chapter(
    val chapter_id: String,
    val has_locked_image: Boolean,
    val image_total: String,
    val index: String,
    val is_new: Int,
    val name: String,
    val pass_time: Int,
    val price: String,
    val release_time: String,
    val size: String,
    val type: String,
    val zip_high_webp: String,
    var isRead: Boolean = false,
): Parcelable

data class Comic(
    val accredit: Int,
    val author: Author,
    val cate_id: String,
    val classifyTags: List<ClassifyTag>,
    val comic_id: String,
    val cover: String,
    val description: String,
    val is_vip: String,
    val last_update_time: Int,
    val last_update_week: String,
    val name: String,
    val ori: String,
    val series_status: Int,
    val short_description: String,
    val status: Int,
    val tagList: List<String>,
    val theme_ids: List<String>,
    val thread_id: String,
    val type: String,
    val week_more: String,
    val wideCover: String?
)

data class Author(
    val avatar: String,
    val id: String,
    val name: String
)

data class ClassifyTag(
    val argName: String,
    val argVal: Int,
    val name: String
)
