package com.benyq.guochat.openeye.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 社区-推荐列表，响应实体类。
 *
 * @author vipyinzhiwei
 * @since  2020/5/6
 */
@Parcelize
data class CommunityRecommend(val itemList: List<Item>, val nextPageUrl: String?, val adExist: Boolean) : Parcelable{

    @Parcelize
    data class Item(val `data`: Data, val type: String): Parcelable

    @Parcelize
    data class Data(val content: Content, val dataType: String, val itemList: List<ItemX>?): Parcelable

    @Parcelize
    data class Content(val `data`: DataX, val type: String): Parcelable

    @Parcelize
    data class ItemX(val `data`: DataXX, val type: String): Parcelable

    @Parcelize
    data class DataX(
        val collected: Boolean,
        val consumption: Consumption,
        val cover: Cover,
        val createTime: Long = 0,
        val dataType: String,
        val description: String,
        val height: Int = 0,
        val id: Long = 0,
        val library: String,
        val owner: Owner,
        val playUrl: String,
        val playUrlWatermark: String,
        val privateMessageActionUrl: String,
        val resourceType: String,
        val tags: List<Tag>?,
        val title: String,
        val type: String,
        val updateTime: Long,
        val url: String,
        val urls: List<String>?,
        val width: Int = 0
    ): Parcelable

    @Parcelize
    data class Owner(
        val actionUrl: String,
        val avatar: String,
        val cover: String,
        val followed: Boolean,
        val ifPgc: Boolean,
        val library: String,
        val nickname: String
    ): Parcelable

    @Parcelize
    data class DataXX(
        val actionUrl: String?,
        val autoPlay: Boolean,
        val bgPicture: String,
        val dataType: String,
        val description: String,
        val image: String,
        val shade: Boolean,
        val subTitle: String,
        val title: String
    ): Parcelable

}