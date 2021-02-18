package com.benyq.guochat.chat.function.fingerprint

import android.os.Build
import androidx.annotation.RequiresApi

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note  暂时不实现，等以后加上面部识别
 */
@RequiresApi(Build.VERSION_CODES.P)
class FingerprintAndroidP : IFingerprint {
    override fun canAuthenticate(): Boolean {
        return true
    }

    override fun authenticate() {
    }

    override fun closeAuthenticate() {

    }
}