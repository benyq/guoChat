package com.benyq.guochat.wanandroid.model

/**
 *
 * @author benyq
 * @date 2021/9/2
 * @email 1520063035@qq.com
 *
 */

data class WechatAuthorData(
    val courseId: String,
    val id: String,
    val name: String,
    val order: Int,
    val parentChapterId: String,
    val userControlSetTop: Boolean,
    val visible: Int,
)