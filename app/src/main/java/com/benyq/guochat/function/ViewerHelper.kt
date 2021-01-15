package com.benyq.guochat.function

import android.content.Context
import android.util.LongSparseArray
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.iielse.imageviewer.ImageViewerBuilder
import com.github.iielse.imageviewer.adapter.ItemType
import com.github.iielse.imageviewer.core.DataProvider
import com.github.iielse.imageviewer.core.ImageLoader
import com.github.iielse.imageviewer.core.Photo
import com.github.iielse.imageviewer.core.Transformer

/**
 * @author benyq
 * @time 2021/1/10
 * @e-mail 1520063035@qq.com
 * @note 显示图片的ImageViewer
 */
object ViewerHelper {

//    fun provideImageViewerBuilder(context: Context, data: List<String>): ImageViewerBuilder {
//        val builder = ImageViewerBuilder(context,
//        initKey = 1L,dataProvider = dataProvider(data), imageLoader = imageLoader(), transformer = )
//
//        return builder
//    }

    private fun dataProvider(data: List<String>): DataProvider {
        return object: DataProvider{
            override fun loadInitial(): List<Photo> {
                var id = 0L
                return data.map {
                    PhotoData(id++, it)
                }
            }
        }
    }

    private fun imageLoader(): ImageLoader {
        return object: ImageLoader {
            override fun load(view: ImageView, data: Photo, viewHolder: RecyclerView.ViewHolder) {
                val it = (data as PhotoData).url
                Glide.with(view).load(it)
                    .override(view.width, view.height)
                    .placeholder(view.drawable)
                    .into(view)
            }
        }
    }

    /**
     * 维护Transition过渡动画的缩略图和大图之间的映射关系. 需要在Activity/Fragment释放时刻.清空此界面的View引用
     */
    private fun transformer(): Transformer {

        return object : Transformer {

            private val map = mutableMapOf<String, LongSparseArray<ImageView>?>() // 可能有多级页面
            val KEY_MAIN = "page_main"

            fun provideTransitionViewsRef(key: String): LongSparseArray<ImageView> {
                return map[key] ?: LongSparseArray<ImageView>().also { map[key] = it }
            }

            // invoke when activity onDestroy or fragment onDestroyView
            fun releaseTransitionViewRef(key: String) {
                map[key] = null
            }

            override fun getView(key: Long): ImageView? {
                return null
            }
        }
    }


}

private class PhotoData(val id: Long, val url: String): Photo {
    override fun id() = id

    override fun itemType() = ItemType.PHOTO

}
