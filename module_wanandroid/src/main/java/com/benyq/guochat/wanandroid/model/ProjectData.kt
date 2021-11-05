package com.benyq.guochat.wanandroid.model

/**
 *
 * @author benyq
 * @date 2021/8/30
 * @email 1520063035@qq.com
 *
 */

data class ProjectTreeData(
    val courseId: String,
    val id: String,
    val name: String,
    val order: Int,
    val parentChapterId: String,
    val userControlSetTop: Boolean,
    val visible: Int
)

