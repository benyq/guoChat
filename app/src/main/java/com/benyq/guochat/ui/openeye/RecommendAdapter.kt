package com.benyq.guochat.ui.openeye

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.benyq.guochat.R
import com.benyq.guochat.loadImage
import com.benyq.guochat.model.bean.openeye.CommunityRecommend
import com.benyq.module_base.ext.gone
import com.benyq.module_base.ext.setDrawable
import com.benyq.module_base.ext.visible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_community_columns_card_follow_card_type.view.*

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
        holder.itemView.tvChoiceness.gone()
        holder.itemView.ivPlay.gone()

        holder.itemView.ivRoundAvatar.loadImage(
            item.data.content.data.owner.avatar,
            isCircle = true
        )

        holder.itemView.tvDescription.text = item.data.content.data.description
        holder.itemView.tvNickName.text = item.data.content.data.owner.nickname
        holder.itemView.tvCollectionCount.text =
            item.data.content.data.consumption.collectionCount.toString()
        val drawable =
            ContextCompat.getDrawable(holder.itemView.context, R.drawable.ic_circle_not_like)
        holder.itemView.tvCollectionCount.setDrawable(drawable, 17f, 17f, 2)

        if (item.data.content.data.library == "DAILY") holder.itemView.tvChoiceness.visible()

        when (item.data.content.type) {
            "video" -> {
                holder.itemView.ivPlay.visible()
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
        holder.itemView.ivBgPicture.run {
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