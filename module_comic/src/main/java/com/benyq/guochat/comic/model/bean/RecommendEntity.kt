package com.benyq.guochat.comic.model.bean

/**
 * @author benyq
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
data class RecommendEntity(
    val comicLists: List<ComicLists>,
    val editTime: String,
    val galleryItems: List<String>,
    val textItems: List<String>
) {
    data class ComicLists(
        val argName: String,
        val argType: Int,
        val argValue: Int,
        val canedit: Int,
        val comicType: Int,
        val comics: List<Comic>,
        val description: String,
        val itemTitle: String,
        val newTitleIconUrl: String,
        val sortId: String,
        val titleIconUrl: String
    ) {
        data class Comic(
            val author_name: String,
            val comicId: Int,
            val content: String,
            val cornerInfo: String,
            val cover: String,
            val description: String,
            val ext: List<Ext>,
            val id: Int,
            val is_vip: Int,
            val linkType: Int,
            val name: String,
            val short_description: String,
            val subTitle: String,
            val tags: List<String>,
            val title: String
        ) {

            data class Ext(
                val key: String,
                val `val`: String
            )
        }

    }

}


