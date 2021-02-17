package com.benyq.guochat.media.video.filter

import com.benyq.guochat.media.video.filter.FilterType

/**
 * @author benyqYe
 * date 2021/1/25
 * e-mail 1520063035@qq.com
 * description TODO
 */

data class VideoFilter(val name: String, val type: FilterType, var checked: Boolean = false)