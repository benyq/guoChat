package com.benyq.guochat.ui.common.widget.satellite_menu

import android.view.View
import androidx.annotation.ColorRes

/**
 * @author benyq
 * @time 2020/6/11
 * @e-mail 1520063035@qq.com
 * @note
 */
data class MenuItem(
    @ColorRes
    val bgColor: Int,
    val icon: Int,
    val label: String,
    val itemClickListener: View.OnClickListener?,
    @ColorRes
    val textColor: Int = android.R.color.white,
    val diameter: Int = 80
)