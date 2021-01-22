package com.benyq.guochat.function.video.filter

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

enum class FilterType {
    FILTER_NONE,
    FILTER_BLACK_WHITE,
    FILTER_MOSAIC,
    FILTER_SOUL,
    FILTER_ZOOM_1,
    FILTER_ZOOM_2,
    FILTER_SKIN_NEEDLING,
    FILTER_LUMINANCE_THRESHOLD,
    FILTER_CARTOON,
}