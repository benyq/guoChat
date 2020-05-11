package com.benyq.guochat.local.entity

import com.benyq.guochat.app.CHAT_TYPE_CONTRACT
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note 联系人
 */
@Entity
data class ContractEntity(
    @Id var id: Long = 0,
    /**
     * 后台数据库中的Id
     */
    val contractId: String = "",
    /**
     * 联系人名字
     */
    val contractName: String = "",
    /**
     * 备注
     */
    val nick: String = contractName,

    val avatarUrl: String = "",

    var chatType: Int = CHAT_TYPE_CONTRACT

)