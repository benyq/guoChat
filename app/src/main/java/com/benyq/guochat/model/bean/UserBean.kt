package com.benyq.guochat.model.bean

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
    val chatId: String = "",
    /**
     * 果聊号
     */
    val chatNo: String = "",
    /**
     * 昵称
     */
    var nickName: String = "",
    /**
     * 头像url
     */
    var avatarUrl: String = ""
) : Serializable