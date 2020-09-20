package com.benyq.guochat.ui.contracts

import android.widget.ImageView
import com.benyq.guochat.R
import com.benyq.guochat.model.bean.ContractSectionBean
import com.benyq.mvvm.ext.dip2px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractsSectionAdapter :
    BaseSectionQuickAdapter<ContractSectionBean, BaseViewHolder>(R.layout.item_contract_header) {

    init {
        setNormalLayout(R.layout.item_contract)
    }

    override fun convert(holder: BaseViewHolder, item: ContractSectionBean) {
        holder.setText(R.id.tvContractName, item.contractEntity?.nick)
        val ivAvatar: ImageView = holder.getView(R.id.ivAvatar)
        Glide.with(context).load(item.contractEntity?.avatarUrl)
            .apply(
                RequestOptions.bitmapTransform(
                    RoundedCorners(
                        context.dip2px(5).toInt()
                    )
                )
            )
            .into(ivAvatar)
    }

    override fun convertHeader(helper: BaseViewHolder, item: ContractSectionBean) {
        helper.setText(R.id.tvHeader, item.headText)
    }
}