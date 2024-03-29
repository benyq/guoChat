package com.benyq.guochat.chat.model.bean

import com.benyq.guochat.database.entity.chat.ContractEntity
import com.benyq.guochat.database.entity.chat.ConversationEntity
import com.chad.library.adapter.base.entity.SectionEntity

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note
 */
data class ContractSectionBean(val contractEntity: ContractEntity? = null, val conversationEntity: ConversationEntity? = null, val headText: String = "", val header: Boolean = false): SectionEntity {

    override val isHeader: Boolean
        get() = header

}