package com.benyq.guochat.chat.function.barcode

import android.annotation.SuppressLint
import android.media.Image
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

/**
 *
 * @author benyq
 * @date 2021/11/3
 * @email 1520063035@qq.com
 *
 */

class QRCodeImageAnalyzer(private val listener: QRCodeFoundListener) : ImageAnalysis.Analyzer {
    private val scanner: BarcodeScanner

    var isStop = false

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage: Image? = imageProxy.image
        if (mediaImage != null) {
            if (isStop) {
                mediaImage.close()
                imageProxy.close()
                return
            }
            try {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.size > 0) listener.onQRCodeFound(
                            barcodes[0].displayValue
                        ) else listener.onCodeNotFound()
                    }
                    .addOnFailureListener { e ->
                        listener.onFailure(e)
                    }
                    .addOnCompleteListener {
                        mediaImage.close()
                        imageProxy.close()
                    }
            } catch (e: Exception) {
                listener.onFailure(e)
            }
        }
    }

    init {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC).build()
        scanner = BarcodeScanning.getClient(options)
    }
}