package com.benyq.guochat.media.video.filter


object FilterFactory {

    fun createFilter(type: FilterType): BaseFilter? {
        return when(type) {
            FilterType.FILTER_BLACK_WHITE -> GrayFilter()
            FilterType.FILTER_MOSAIC -> MosaicFilter()
            FilterType.FILTER_SOUL -> SoulFilter()
            FilterType.FILTER_ZOOM_1 -> ZoomFilter()
            FilterType.FILTER_ZOOM_2 -> ZoomFilter2()
            FilterType.FILTER_SKIN_NEEDLING -> SkinNeedlingFilter()
            FilterType.FILTER_LUMINANCE_THRESHOLD -> LuminanceThresholdFilter()
            FilterType.FILTER_CARTOON -> CartoonFilter()
            else -> null
        }
    }

}

enum class FilterType(val filterName: String) {
    FILTER_NONE("无效果"),
    FILTER_BLACK_WHITE("黑白"),
    FILTER_MOSAIC("马赛克"),
    FILTER_SOUL("灵魂出窍"),
    FILTER_ZOOM_1("抖动一"),
    FILTER_ZOOM_2("抖动二"),
    FILTER_SKIN_NEEDLING("毛刺"),
    FILTER_LUMINANCE_THRESHOLD("素描"),
    FILTER_CARTOON("卡通");


    companion object {
        //选了几个我想用的滤镜
        fun provideFilters() : MutableList<VideoFilter>{
            val filters = mutableListOf<VideoFilter>()
            filters.add(VideoFilter(FILTER_NONE.filterName, FILTER_NONE))
            filters.add(VideoFilter(FILTER_BLACK_WHITE.filterName, FILTER_BLACK_WHITE))
            filters.add(VideoFilter(FILTER_MOSAIC.filterName, FILTER_MOSAIC))
            filters.add(VideoFilter(FILTER_CARTOON.filterName, FILTER_CARTOON))
            filters.add(VideoFilter(FILTER_SOUL.filterName, FILTER_SOUL))
            filters.add(VideoFilter(FILTER_LUMINANCE_THRESHOLD.filterName, FILTER_LUMINANCE_THRESHOLD))
            filters.add(VideoFilter(FILTER_SKIN_NEEDLING.filterName, FILTER_SKIN_NEEDLING))
            filters.add(VideoFilter(FILTER_ZOOM_1.filterName, FILTER_ZOOM_1))
            return filters
        }
    }
}