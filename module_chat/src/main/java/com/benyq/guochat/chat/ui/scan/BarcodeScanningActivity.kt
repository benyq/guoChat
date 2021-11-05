package com.benyq.guochat.chat.ui.scan

import android.os.Bundle
import android.util.Size
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.app.chatIdPrefix
import com.benyq.guochat.chat.databinding.ActivityBarcodeScanningBinding
import com.benyq.guochat.chat.function.barcode.QRCodeFoundListener
import com.benyq.guochat.chat.function.barcode.QRCodeImageAnalyzer
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.guochat.chat.model.vm.ContractViewModel
import com.benyq.guochat.chat.ui.contracts.ApplyContractActivity
import com.benyq.guochat.chat.ui.contracts.ContractDetailActivity
import com.benyq.module_base.ext.*
import com.benyq.module_base.ui.base.LifecycleActivity
import com.google.common.util.concurrent.ListenableFuture
import com.gyf.immersionbar.ImmersionBar
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

/**
 *
 * @author benyq
 * @date 2021/11/3
 * @email 1520063035@qq.com
 *
 */
@AndroidEntryPoint
class BarcodeScanningActivity :
    LifecycleActivity<ContractViewModel, ActivityBarcodeScanningBinding>() {
    override fun provideViewBinding(): ActivityBarcodeScanningBinding =
        ActivityBarcodeScanningBinding.inflate(layoutInflater)

    override fun initVM(): ContractViewModel = getViewModel()

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null
    private var camera: Camera? = null

    private val cameraExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    private var enableTorch = false

    override fun isHideBar() = true

    private lateinit var qrCodeImageAnalyzer: QRCodeImageAnalyzer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 保持Activity处于唤醒状态
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun initView() {
        binding.ivClose.setOnClickListener {
            finish()
        }
        binding.ibFlashLight.setOnClickListener {
            enableTorch = !enableTorch
            camera?.cameraControl?.enableTorch(enableTorch)
        }
        resizeViewMargin()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun dataObserver() {
        with(viewModelGet()) {
            loadingType.observe(this@BarcodeScanningActivity) {
                when {
                    it.isLoading -> showLoading(it.isSuccess)
                    !it.isLoading -> hideLoading()
                    it.isError != null -> Toasts.show(it.isError?.message ?: "未知错误")
                }
                finish()
            }
            searchContractData.observe(this@BarcodeScanningActivity) { contract ->
                contract?.run {
                    if (id > 0) {
                        goToActivity<ContractDetailActivity>(IntentExtra.contractData to this)
                    } else {
                        goToActivity<ApplyContractActivity>(IntentExtra.contractData to this)
                    }
                }
            }
        }
    }

    private fun startCamera() {

        val previewView = binding.viewFinder
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture?.addListener({
            try {
                val cameraProvider = cameraProviderFuture?.get()
                val preview = Preview.Builder()
                    .build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetRotation(preview.targetRotation)
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                camera = cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis,
                    preview
                )
                qrCodeImageAnalyzer = QRCodeImageAnalyzer(object : QRCodeFoundListener {
                    override fun onQRCodeFound(qrCode: String?) {
                        qrCode?.let {
                            cameraProvider?.unbindAll()
                            qrCodeImageAnalyzer.isStop = true
                            dealCode(it)
                        }
                    }

                    override fun onCodeNotFound() {
                        loge("code onCodeNotFound")
                    }

                    override fun onFailure(e: Exception?) {
                        loge("code onFailure ${e?.message}")
                    }
                })

                imageAnalysis.setAnalyzer(
                    cameraExecutor,
                    qrCodeImageAnalyzer
                )
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun dealCode(code: String) {
        when {
            code.startsWith(chatIdPrefix) -> {
                viewModelGet().searchContractByCode(ChatLocalStorage.uid, code)
            }
        }
    }

    private fun resizeViewMargin() {

        if (checkFullScreenPhone()) {
            val topMargin = dip2px(15).toInt() + ImmersionBar.getStatusBarHeight(this)

            val ivCloseParam = binding.ivClose.layoutParams as ConstraintLayout.LayoutParams
            ivCloseParam.topMargin = topMargin
            binding.ivClose.layoutParams = ivCloseParam
        }
    }
}