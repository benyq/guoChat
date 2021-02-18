package com.benyq.guochat.chat.function.fingerprint

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note
 */
interface IFingerprint {

    /**
     * 检测指纹硬件是否可用，及是否添加指纹
     */
    fun canAuthenticate(): Boolean

    /**
     * 指纹验证
     */
    fun authenticate()

    /**
     * 停止指纹验证
      */
    fun closeAuthenticate()
}