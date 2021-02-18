package com.benyq.guochat.chat.model.bean

import java.io.Serializable

/**
 * @author benyq
 * @time 2020/8/24
 * @e-mail 1520063035@qq.com
 * @note
 */

data class PersonConfig(
    val phoneNumber: String,
    var fingerprintLogin: Boolean
) : Serializable {
    companion object {
        fun defaultConfig(): PersonConfig = PersonConfig("", false)
    }
}