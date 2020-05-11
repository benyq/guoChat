package com.benyq.guochat.ui.chats

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.ProgressBar
import com.benyq.guochat.function.other.DateFormatUtil
import com.benyq.guochat.R
import com.benyq.guochat.dip2px
import com.benyq.guochat.local.entity.ChatRecordEntity
import com.benyq.mvvm.ext.getDrawableRef
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder


/**
 * @author benyq
 * @time 2020/4/26
 * @e-mail 1520063035@qq.com
 * @note
 */
//uid 是当前帐号id
class ChatRecordAdapter(private val uid: Int) :
    BaseDelegateMultiAdapter<ChatRecordEntity, BaseViewHolder>() {

    private var contractAvatar =
        "http://i2.hdslb.com/bfs/face/d79637d472c90f45b2476871a3e63898240a47e3.jpg"
    private var ownAvatar =
        "http://i2.hdslb.com/bfs/face/d79637d472c90f45b2476871a3e63898240a47e3.jpg"

    /**
     * 正在播放语音
     */
    private var mPlayingRecord: ChatRecordEntity? = null

    init {

        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ChatRecordEntity>() {
            override fun getItemType(data: List<ChatRecordEntity>, position: Int): Int {
                return if (data[position].fromUid == uid) {
                    0
                } else {
                    1
                }
            }
        })
        getMultiTypeDelegate()?.run {
            addItemType(0, R.layout.item_chat_record_right)
                .addItemType(1, R.layout.item_chat_record_left)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ChatRecordEntity) {
        val ivAvatar = helper.getView<ImageView>(R.id.ivAvatar)

        when (helper.itemViewType) {
            0 -> {
                Glide.with(context).load(ownAvatar)
                    .apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(
                                dip2px(
                                    context,
                                    5
                                ).toInt()
                            )
                        )
                    )
                    .into(ivAvatar)
            }
            1 -> {
                Glide.with(context).load(contractAvatar)
                    .apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(
                                dip2px(
                                    context,
                                    5
                                ).toInt()
                            )
                        )
                    )
                    .into(ivAvatar)
            }
        }

        item.run {
            helper.setText(R.id.tvUpdateTime, DateFormatUtil.dateLongToString(sendTime))
            when (chatType) {
                ChatRecordEntity.TYPE_IMG -> {
                    helper.setGone(R.id.ivContent, false)
                        .setGone(R.id.tvContent, true)
                        .setGone(R.id.llVoice, true)
                    val ivContent = helper.getView<ImageView>(R.id.ivContent)
                    Glide.with(context).load(imgUrl)
                        .override(dip2px(context, 300).toInt(), dip2px(context, 150).toInt())
                        .into(ivContent)
                }
                ChatRecordEntity.TYPE_VOICE -> {
                    helper.setGone(R.id.llVoice, false)
                        .setGone(R.id.ivContent, true)
                        .setGone(R.id.tvContent, true)
                        .setText(R.id.tvVoiceDuration, "${voiceRecordDuration / 1000}\" ")
                    val pb = helper.getView<ProgressBar>(R.id.pbVoice)
                    val drawable: Drawable? = if (item.id == mPlayingRecord?.id ?: 0) {
                        context.getDrawableRef(R.drawable.anim_voice_play)
                    }else {
                        context.getDrawableRef(R.drawable.ic_volume_3)
                    }

                    val bounds: Rect = pb.indeterminateDrawable.bounds // re-use bounds from current drawable

                    pb.indeterminateDrawable = drawable // set new drawable

                    pb.indeterminateDrawable.setBounds(bounds) // set bounds to new drawable

                }
                //ChatRecordBean.TYPE_TEXT
                else -> {
                    helper.setGone(R.id.tvContent, false)
                        .setGone(R.id.ivContent, true)
                        .setGone(R.id.llVoice, true)
                        .setText(R.id.tvContent, content)
                }
            }


        }
    }


    fun setVoiceStop(recordId: Long = mPlayingRecord?.id ?: 0) {
        data.forEachIndexed { index, chatRecordEntity ->
            if (chatRecordEntity.id == recordId) {
                mPlayingRecord = null
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }

    fun setVoicePlay(record: ChatRecordEntity) {
        mPlayingRecord = record
        data.forEachIndexed { index, chatRecordEntity ->
            if (chatRecordEntity.id == record.id) {
                notifyItemChanged(index)
                return@forEachIndexed
            }
        }
    }
}