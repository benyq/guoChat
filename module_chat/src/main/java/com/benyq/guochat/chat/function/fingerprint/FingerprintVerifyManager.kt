package com.benyq.guochat.chat.function.fingerprint

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import com.benyq.module_base.ext.fromM

/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class FingerprintVerifyManager(
    context: Context,
    successAction: () -> Unit,
    private val failedAction: (String) -> Unit
) {

    companion object {
        fun canAuthenticate(context: Context): Boolean {
            return if (fromM()) {
                //硬件是否支持指纹识别
                //是否已添加指纹
                FingerprintManagerCompat.from(context).isHardwareDetected && FingerprintManagerCompat.from(context).hasEnrolledFingerprints()
            } else {
                false
            }
        }
    }

    private var fingerprint: IFingerprint? = null

    init {
        when {
//            fromP() -> {
//                fingerprint = FingerprintAndroidP()
//            }
            fromM() -> {
                fingerprint = FingerprintAndroidM(context, successAction, failedAction)
            }
        }
    }

    fun authenticate() {
        fingerprint?.authenticate() ?: let {
            failedAction.invoke("")
        }
    }

    fun closeAuthenticate() {
        fingerprint?.closeAuthenticate()
    }
}