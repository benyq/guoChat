package com.benyq.guochat.chat.function.barcode

/**
 *
 * @author benyq
 * @date 2021/11/3
 * @email 1520063035@qq.com
 *
 */
interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun onCodeNotFound()
    fun onFailure(e: Exception?)
}