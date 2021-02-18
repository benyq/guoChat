package com.benyq.guochat.chat.model.bean

import com.benyq.guochat.chat.local.entity.ContractEntity
import com.chad.library.adapter.base.entity.SectionEntity

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ContractSectionBean(val contractEntity: ContractEntity?, val headText: String = "", val header: Boolean = false): SectionEntity {

    override val isHeader: Boolean
        get() = header

}