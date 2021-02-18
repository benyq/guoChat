package com.benyq.guochat.chat.function.fingerprint

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal


/**
 * @author benyq
 * @time 2020/8/23
 * @e-mail 1520063035@qq.com
 * @note android 6.0 指纹识别
 */
@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintAndroidM(context: Context, private val successAction: ()->Unit, private val failedAction: (String)->Unit) : IFingerprint {

    private val fingerprintManagerCompat: FingerprintManagerCompat = FingerprintManagerCompat.from(context)

    //用于取消扫描器的扫描动作
    private var cancellationSignal: CancellationSignal? = CancellationSignal()

    //指纹加密
    private val cryptoObject: FingerprintManagerCompat.CryptoObject = FingerprintManagerCompat.CryptoObject(CipherHelper().createCipher())

    override fun canAuthenticate() : Boolean{
        /*
        * 硬件是否支持指纹识别
        * */
        if (!fingerprintManagerCompat.isHardwareDetected) {
            return false
        }
        //是否已添加指纹
        if (!fingerprintManagerCompat.hasEnrolledFingerprints()) {
            return false
        }
        return true
    }

    override fun authenticate() {
        if (!canAuthenticate()) {
            failedAction.invoke("不支持指纹识别")
            return
        }
        if (cancellationSignal == null) {
            cancellationSignal = CancellationSignal()
        }
        //调起指纹验证
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null)

    }

    override fun closeAuthenticate() {
        cancellationSignal?.let {
            if (it.isCanceled) {
                it.cancel()
            }
        }
    }

    /**
     * 指纹验证结果回调
     */
    private val authenticationCallback: FingerprintManagerCompat.AuthenticationCallback =
        object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationError(
                errMsgId: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errMsgId, errString)
                //errMsgId==5时，在OnDialogActionListener的onCancle回调中处理；！=5的报错，才需要显示在指纹验证框中。
                if (errMsgId != 5)  {
                    failedAction.invoke(errString.toString())
                }
            }

            override fun onAuthenticationHelp(
                helpMsgId: Int,
                helpString: CharSequence
            ) {
                super.onAuthenticationHelp(helpMsgId, helpString)

            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                successAction.invoke()

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                failedAction.invoke("")
            }
        }
}