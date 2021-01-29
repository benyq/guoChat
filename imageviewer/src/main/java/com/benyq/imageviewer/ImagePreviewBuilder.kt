package com.benyq.imageviewer

import android.content.Context
import android.content.Intent
import android.view.View
import com.benyq.mvvm.ext.logw

/**
 * @author benyqYe
 * date 2021/1/27
 * e-mail 1520063035@qq.com
 * description 图片预览构建
 */

object ImagePreview {

    fun setFullScreen(fullScreen: Boolean) : ImagePreview{
        Components.isFullScreen = fullScreen
        return this
    }

    fun setData(data: List<PreviewPhoto>): ImagePreview{
        Components.data.addAll(data)
        return this
    }

    fun setCacheView(view: List<View>): ImagePreview {
        Components.cacheView.addAll(view)
        return this
    }

    fun setCurPosition(position: Int) : ImagePreview{
        Components.curPosition = if (position < 0) 0 else position
        return this
    }

    fun show(context: Context) {
        if (!checkParam()){
            return
        }
        val intent = Intent(context, PreviewPhotoActivity::class.java)
        context.startActivity(intent)
    }

    //数据检查
    private fun checkParam() : Boolean{

        if (Components.data.isEmpty()) {
            logw("图片或视频数据不能为空")
            return false
        }

        if (Components.cacheView.isEmpty()) {
            logw("view 数据不能为空")
            return false
        }
        return true
    }
}
