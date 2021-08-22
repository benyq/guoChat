package com.benyq.guowanandroid.model

/**
 *
 * @author benyq
 * @date 2021/8/10
 * @email 1520063035@qq.com
 */

/*
* {"desc":"扔物线",
* "id":29,
* "imagePath":"https://wanandroid.com/blogimgs/18320a47-148a-4f8e-bf1a-71e633872dcf.png",
* "isVisible":1,
* "order":0,
* "title":"Android 面试黑洞&mdash;&mdash;当我按下 Home 键再切回来，会发生什么？",
* "type":0,
* "url":"https://www.bilibili.com/video/BV1CA41177Se"}
*/

data class BannerData(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)