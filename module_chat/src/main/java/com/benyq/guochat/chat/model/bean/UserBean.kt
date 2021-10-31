package com.benyq.guochat.chat.model.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 用户信息类
 */
data class UserBean(
    /**
     * 果聊号
     */
    @SerializedName("id")
    val chatId: String,
    /**
     * 果聊号
     */
    val chatNo: String,
    /**
     * 昵称
     */
    var nick: String,
    /**
     * 头像url
     */
    @SerializedName("avatar")
    var avatarUrl: String?,
    var token: String,
    var phone: String,
    var gender: Int = 2,
) : Serializable