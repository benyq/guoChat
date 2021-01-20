package com.benyq.guochat.function.video.filter

object FilterFactory {

    fun createFilter(type: FilterType): BaseFilter? {
        return when(type) {
            FilterType.FILTER_BLACK_WHITE -> GrayFilter()
            FilterType.FILTER_MOSAIC -> MosaicFilter()
            else -> null
        }
    }

}

enum class FilterType {
    FILTER_NONE,
    FILTER_BLACK_WHITE,
    FILTER_MOSAIC,
}