package com.benyq.guochat.chat.ui.contracts

import android.widget.ImageView
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.loadAvatar
import com.benyq.guochat.chat.model.bean.ContractSectionBean
import com.benyq.module_base.ext.dip2px
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
        ivAvatar.loadAvatar(item.contractEntity?.avatarUrl, round = 5)
    }

    override fun convertHeader(helper: BaseViewHolder, item: ContractSectionBean) {
        helper.setText(R.id.tvHeader, item.headText)
    }
}