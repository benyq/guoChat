package com.benyq.imageviewer

import android.graphics.Color
import com.benyq.mvvm.ext.loge

/**
 * @author benyqYe
 * date 2021/1/28
 * e-mail 1520063035@qq.com
 * description 颜色处理工具
 */

internal object ColorTool {

    @JvmStatic
    fun getColorWithAlpha(baseColor: Int, alpha: Float): Int {

        val alphaColor = 0X00FFFFFF and baseColor
        val a = (255 * alpha).toInt() shl 24
        return a or alphaColor
    }
}