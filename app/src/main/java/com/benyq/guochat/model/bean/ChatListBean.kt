package com.benyq.guochat.model.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
@Parcelize
data class ChatListBean(
    val avatar: String,
    val contractName: String,
    /**
     * 类型， 1 联系人， 2 群聊
     */
    val chatType: Int,
    val latestTime: Long,
    val latestConversation: String,
    val notificationOff: Boolean = false,
    val fromToId: Long = 0
) : Parcelable