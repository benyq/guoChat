package com.benyq.guochat.chat.ui.contracts

import android.widget.ImageView
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.loadAvatar
import com.benyq.guochat.chat.model.bean.ContractBean
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 *
 * @author benyq
 * @date 2021/11/4
 * @email 1520063035@qq.com
 *
 */
class ApplyContractRecordAdapter : BaseQuickAdapter<ContractBean, BaseViewHolder>(R.layout.item_apply_contract_record){

    init {
        addChildClickViewIds(R.id.btnAgree, R.id.btnRefuse)
    }

    override fun convert(holder: BaseViewHolder, item: ContractBean) {
        holder.getView<ImageView>(R.id.ivAvatar).loadAvatar(item.avatar)
        holder.setText(R.id.tvContractName, item.nick)
    }
}