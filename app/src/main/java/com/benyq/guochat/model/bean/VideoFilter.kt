package com.benyq.guochat.model.bean

import com.benyq.guochat.function.video.filter.FilterType

/**
 * @author benyqYe
 * date 2021/1/25
 * e-mail 1520063035@qq.com
 * description TODO
 */

data class VideoFilter(val name: String, val type: FilterType, var checked: Boolean = false)