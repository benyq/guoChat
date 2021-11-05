package com.benyq.guochat.chat.model.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @author benyq
 * @date 2021/10/28
 * @email 1520063035@qq.com
 *
 */
@Parcelize
data class ContractBean(
    val id: Long = 0,
    //联系人所有人id
    val uid: String? = null,
    //联系人id
    val contractId: String,
    //联系人果聊号
    val chatNo: String,
    val nick: String,
    val remark: String? = null,
    val gender: Int = 2,
    var avatar: String? = null
): Parcelable
