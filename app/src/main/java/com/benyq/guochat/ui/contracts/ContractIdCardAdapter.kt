package com.benyq.guochat.ui.contracts

import android.widget.ImageView
import com.benyq.guochat.R
import com.benyq.guochat.app.GENDER_FEMALE
import com.benyq.guochat.app.GENDER_MALE
import com.benyq.guochat.model.bean.ContractSectionBean
import com.benyq.module_base.ext.dip2px
import com.benyq.module_base.ext.loadImage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author benyq
 * @time 2020/8/19
 * @e-mail 1520063035@qq.com
 * @note
 */
class ContractIdCardAdapter :
    BaseQuickAdapter<ContractSectionBean, BaseViewHolder>(R.layout.item_contract_id_card) {

    override fun convert(holder: BaseViewHolder, item: ContractSectionBean) {
        val contractEntity = item.contractEntity
        contractEntity?.run {
            val ivAvatar = holder.getView<ImageView>(R.id.ivAvatar)
            ivAvatar.loadImage(avatarUrl, context.dip2px(5).toInt())

            holder.setText(R.id.tvContractName, contractName)
            val gender = when (gender) {
                GENDER_FEMALE -> "女性"
                GENDER_MALE -> "男性"
                else -> "阴阳人"
            }
            holder.setText(R.id.tvContractGender, "性别: $gender")
        }

    }

}