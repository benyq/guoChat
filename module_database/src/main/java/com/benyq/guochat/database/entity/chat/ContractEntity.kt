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
     * 这个联系人的归属对象
     */
    val ownUserId: String = "",
    /**
     * 后台数据库中的Id
     */
    val contractId: String = "",
    var chatNo: String,

    /**
     * 联系人名字
     */
    var nick: String = "",
    /**
     * 性别 0 女性  1 男性  2 隐藏
     * GENDER_UNKNOWN == 1
     */
    var gender: Int = 1,
    /**
     * 备注
     */
    var remark: String? = null,

    var avatarUrl: String? = null,

    // CHAT_TYPE_CONTRACT == 1
    var chatType: Int = 1

) : Parcelable