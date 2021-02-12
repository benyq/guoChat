package com.benyq.guochat.ui.chats

import com.benyq.guochat.R
import com.benyq.guochat.function.other.DateFormatUtil
import com.benyq.guochat.model.bean.ChatListBean
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.loge
import com.benyq.module_base.glide.ProgressInterceptor
import com.benyq.module_base.glide.ProgressListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class ChatAdapter : BaseQuickAdapter<ChatListBean, BaseViewHolder>(R.layout.item_chat_list) {
    override fun convert(holder: BaseViewHolder, item: ChatListBean) {
        item.run {
            holder.setText(R.id.tvContractName, contractName)
                .setText(R.id.tvLatestTime, DateFormatUtil.dateLongToString(latestTime))
                .setText(R.id.tvLatestConversion, latestConversation)
                .setVisible(R.id.ivNotificationOff, !notificationOff)

            //Glide 加载图片的进度 具体可参考代码
            ProgressInterceptor.addListener(item.avatar, object : ProgressListener {
                override fun onProgress(progress: Int) {
                    loge("progress $progress")
                }
            })

            Glide.with(context).load(avatar)
                .apply(RequestOptions().apply {
                    skipMemoryCache(true)
                    diskCacheStrategy(DiskCacheStrategy.NONE)
                })
                .transform(RoundedCorners(context.dip2px(10).toInt()))
                .into(holder.getView(R.id.ivAvatar))
        }
    }
}