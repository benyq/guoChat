package com.benyq.guochat.local.entity

import android.os.Parcelable
import com.benyq.guochat.app.CHAT_TYPE_CONTRACT
import com.benyq.guochat.app.GENDER_UNKNOWN
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import kotlinx.android.parcel.Parcelize

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
     */
    val gender: Int = GENDER_UNKNOWN,
    /**
     * 备注
     */
    val nick: String = contractName,

    val avatarUrl: String = "",

    var chatType: Int = CHAT_TYPE_CONTRACT

): Parcelable