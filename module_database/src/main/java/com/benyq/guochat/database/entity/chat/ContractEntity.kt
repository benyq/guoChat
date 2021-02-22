package com.benyq.guochat.database.entity.chat

import android.os.Parcelable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.parcelize.Parcelize

/**
 * @author benyq
 * @time 2020/5/4
 * @e-mail 1520063035@qq.com
 * @note 联系人
 */
@Entity
@Parcelize
data class ContractEntity(
    @Id var id: Long = 0,
    /**
     * 后台数据库中的Id
     */
    val contractId: String = "",
    /**
     * 这个联系人的归属对象
     */
    val ownUserId: String = "",
    /**
     * 联系人名字
     */
    val contractName: String = "",
    /**
     * 性别 0 女性  1 男性  2 隐藏
     * GENDER_UNKNOWN == 1
     */
    val gender: Int = 1,
    /**
     * 备注
     */
    val nick: String = contractName,

    val avatarUrl: String = "",

    // CHAT_TYPE_CONTRACT == 1
    var chatType: Int = 1

): Parcelable