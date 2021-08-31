package com.benyq.guochat.wanandroid.model

import com.benyq.guochat.wanandroid.model.ArticleData

/**
 *
 * @author benyq
 * @date 2021/8/30
 * @email 1520063035@qq.com
 *
 */

fun fakeArticleData(): List<ArticleData> {
    val article = ArticleData(
        apkLink = "",
        audit = 1,
        author = "张鸿洋",
        canEdit = false,
        chapterId = 543,
        chapterName = "Android技术周报",
        collect = false,
        courseId = 13,
        desc = "",
        descMd = "",
        envelopePic = "",
        fresh = false,
        host = "",
        id = 19132,
        link = "https://www.wanandroid.com/blog/show/3039",
        niceDate = "2021-07-28 00:00",
        niceShareDate = "2021-07-28 00:10",
        origin = "",
        prefix = "",
        projectLink = "",
        publishTime = 1627401600000,
        realSuperChapterId = 542,
        selfVisible = 0,
        shareDate = 1627402200000,
        shareUser = "",
        superChapterId = 543,
        superChapterName = "技术周报",
        tags = listOf(),
        title = "Android 技术周刊 （2021-07-21 ~ 2021-07-28）",
        type = 0,
        userId = -1,
        visible = 1,
        zan = 0
    )

    return listOf(article, article, article, article, article, article, article)
}