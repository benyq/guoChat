package com.benyq.guochat.chat.function.message

/**
 * @author benyq
 * @time 2021/10/31
 * @e-mail 1520063035@qq.com
 * @note
 */
data class WSMessage(val type: Int, val data: String) {
    companion object {
        var TYPE_HEART = 101
        var TYPE_UID = 102
    }
}