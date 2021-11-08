package com.benyq.guochat.chat.ui.chats

import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.function.other.DateFormatUtil
import com.benyq.guochat.chat.loadAvatar
import com.benyq.guochat.chat.model.bean.ChatListBean
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.gone
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

    private val maxUnreadRecord = "99+"

    override fun convert(holder: BaseViewHolder, item: ChatListBean) {
        item.run {
            holder.setText(R.id.tvContractName, contractName)
                .setText(R.id.tvLatestTime, DateFormatUtil.dateLongToString(latestTime))
                .setText(R.id.tvLatestConversion, latestConversation)
                .setVisible(R.id.ivNotificationOff, !notificationOff)
            val tvNewRecord: TextView = holder.getView(R.id.tvNewRecord)
            val viewWidth: Int
            when {
                unreadRecord <= 0 -> {
                    viewWidth = context.dip2px(15).toInt()
                    tvNewRecord.text = unreadRecord.toString()
                }
                unreadRecord < 10 -> {
                    viewWidth = context.dip2px(15).toInt()
                    tvNewRecord.text = unreadRecord.toString()
                }
                unreadRecord < 100 -> {
                    viewWidth = context.dip2px(20).toInt()
                    tvNewRecord.text = unreadRecord.toString()
                }
                else -> {
                    viewWidth = context.dip2px(25).toInt()
                    tvNewRecord.text = maxUnreadRecord
                }
            }
            tvNewRecord.isGone = unreadRecord <= 0
            tvNewRecord.updateLayoutParams<ConstraintLayout.LayoutParams> {
                width = viewWidth
            }
            //Glide 加载图片的进度 具体可参考代码
            ProgressInterceptor.addListener(item.avatar, object : ProgressListener {
                override fun onProgress(progress: Int) {
                    loge("progress $progress")
                }
            })
            holder.getView<ImageView>(R.id.ivAvatar).loadAvatar(avatar)
        }
    }
}