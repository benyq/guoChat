package com.benyq.guochat.function

import android.content.Context
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.core.DataProvider
import com.github.iielse.imageviewer.core.Photo

/**
 * @author benyq
 * @time 2021/1/10
 * @e-mail 1520063035@qq.com
 * @note 显示图片的ImageViewer
 */
object ViewerHelper {

//    fun provideImageViewerBuilder(context: Context): ImageViewerBuilder {
//        val builder = ImageViewerBuilder(context,
//        initKey = 1L,dataProvider = dataProvider())
//
//        return builder
//    }

    private fun dataProvider(data: List<String>): DataProvider {
        return object: DataProvider{
            override fun loadInitial(): List<Photo> {
                return super.loadInitial()
            }
        }
    }
}