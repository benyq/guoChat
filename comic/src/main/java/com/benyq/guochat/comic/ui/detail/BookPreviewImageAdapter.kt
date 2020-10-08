package com.benyq.guochat.comic.ui.detail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.benyq.guochat.comic.R
import com.benyq.guochat.comic.model.bean.ImageListBean
import com.benyq.mvvm.ext.getScreenWidth
import com.benyq.mvvm.ext.gone
import com.benyq.mvvm.ext.visible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.comic_item_book_preview_image.view.*
import kotlin.math.roundToInt

/**
 * @author benyq
 * @time 2020/10/8
 * @e-mail 1520063035@qq.com
 * @note
 */
class BookPreviewImageAdapter :
    BaseQuickAdapter<ImageListBean, BaseViewHolder>(R.layout.comic_item_book_preview_image) {

    override fun convert(holder: BaseViewHolder, item: ImageListBean) {
        val image = holder.itemView.ivCover
        val height = context.getScreenWidth()
            .toFloat() * item.height / if (item.width != 0) item.width else 1
        val layoutParam = image.layoutParams
        layoutParam.height = height.roundToInt()
        image.layoutParams = layoutParam

        holder.itemView.tvPosition.run {
            text = holder.layoutPosition.toString()
            visible()
        }
        loadImage(context, item.img50, R.color.black, R.color.black, item.width, item.height, object : CustomViewTarget<ImageView, Bitmap>(image) {
            override fun onLoadFailed(errorDrawable: Drawable?) {

            }

            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                holder.itemView.tvPosition.gone()
                holder.itemView.ivCover.setImageBitmap(resource)
            }

            override fun onResourceLoading(placeholder: Drawable?) {
                super.onResourceLoading(placeholder)
                image.setImageDrawable(placeholder)
            }

            override fun onResourceCleared(placeholder: Drawable?) {

            }

        })
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(context).clear(holder.itemView.ivCover)
    }

    private fun loadImage(
        context: Context,
        url: String?,
        placeholder: Int,
        error: Int,
        width: Int = Target.SIZE_ORIGINAL,
        height: Int = Target.SIZE_ORIGINAL,
        viewTag: CustomViewTarget<ImageView, Bitmap>
    ) {
        val options = RequestOptions()
            .placeholder(placeholder)
            .error(error)
            .override(width, height)
        Glide.with(context).asBitmap().load(url).apply(options)
            .into(viewTag)
    }

}

