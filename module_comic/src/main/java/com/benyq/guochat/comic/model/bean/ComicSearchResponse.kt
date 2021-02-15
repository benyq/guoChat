package com.benyq.guochat.comic.model.bean

/**
 * @author benyq
 * @time 2020/9/27
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ComicSearchResponse(
    val comicNum: Int,
    val hasMore: Boolean,
    val page: Int,
    val comics: List<ComicsBean>
) {
    data class ComicsBean(
        val comicId: String,
        val cover: String,
        val passChapterNum: String,
        val name: String,
        val monthTicket: String,
        val clickTotal: String,
        val seriesStatus: String,
        val flag: String,
        val author: String,
        val description: String,
        val conTag: Int,
        val tags: List<String>
    )
}