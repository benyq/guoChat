package com.benyq.guochat.openeye.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.benyq.guochat.openeye.R
import com.benyq.module_base.ext.loadImage
import com.benyq.module_base.ext.setDrawable
import com.benyq.guochat.openeye.bean.CommunityRecommend
import com.benyq.guochat.openeye.ui.activity.OpenEyeUgcDetailActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/9/2
 * @e-mail 1520063035@qq.com
 * @note 去掉其他的，只留下卡片
 */
class RecommendAdapter(private val fragment: Fragment, private val maxImageWidth: Int) :
    BaseQuickAdapter<CommunityRecommend.Item, BaseViewHolder>(R.layout.item_community_columns_card_follow_card_type) {

    val STR_COMMUNITY_COLUMNS_CARD = "communityColumnsCard"
    val STR_FOLLOW_CARD_DATA_TYPE = "FollowCard"


    override fun convert(holder: BaseViewHolder, item: CommunityRecommend.Item) {

        holder.setGone(R.id.tvChoiceness, true)
        holder.setGone(R.id.ivPlay, true)

        holder.getView<ImageView>(R.id.ivRoundAvatar).loadImage(
            item.data.content.data.owner.avatar,
            isCircle = true
        )

        holder.setText(R.id.tvDescription, item.data.content.data.description)
        holder.setText(R.id.tvNickName, item.data.content.data.owner.nickname)
        holder.setText(R.id.tvCollectionCount, item.data.content.data.consumption.collectionCount.toString())

        val drawable =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_circle_not_like)
        holder.getView<TextView>(R.id.tvCollectionCount).setDrawable(drawable, 17f, 17f, 2)

        if (item.data.content.data.library == "DAILY") holder.setVisible(R.id.tvChoiceness, true)

        when (item.data.content.type) {
            "video" -> {
                holder.setVisible(R.id.ivPlay, true)
                holder.itemView.setOnClickListener {
                    val items =
                        data.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                    OpenEyeUgcDetailActivity.start(
                        fragment.requireActivity(),
                        items,
                        holder.layoutPosition
                    )
                }
            }
            "ugcPicture" -> {
                holder.itemView.setOnClickListener {
                    val items =
                        data.filter { it.type == STR_COMMUNITY_COLUMNS_CARD && it.data.dataType == STR_FOLLOW_CARD_DATA_TYPE }
                    OpenEyeUgcDetailActivity.start(
                        fragment.requireActivity(),
                        items,
                        holder.layoutPosition
                    )
                }
            }
            else -> {

            }
        }
        //要计算
        holder.getView<ImageView>(R.id.ivBgPicture).run {
            layoutParams.width = maxImageWidth
            layoutParams.height =
                calculateImageHeight(item.data.content.data.width, item.data.content.data.height)
            loadImage(item.data.content.data.cover.feed, 4)
        }
    }

    private fun calculateImageHeight(originWidth: Int, originHeight: Int): Int {
        return if (originWidth <= 0 || originHeight <= 0) {
            maxImageWidth
        } else {
            maxImageWidth * originHeight / originWidth
        }
    }
}