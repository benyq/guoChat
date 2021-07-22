package com.benyq.guochat.chat.ui.discover

import android.graphics.*
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import com.benyq.guochat.chat.app.IntentExtra
import com.benyq.guochat.chat.databinding.ActivityEditImageBinding
import com.benyq.guochat.core.BitmapUtil
import com.benyq.module_base.ext.*
import com.benyq.module_base.ui.WarningDialog
import com.benyq.module_base.ui.base.BaseActivity
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @author benyqYe
 * date 2021/5/11
 * e-mail 1520063035@qq.com
 * description 图片编辑
 */

class EditImageActivity : BaseActivity<ActivityEditImageBinding>() {

    override fun isHideBar() = true

    private lateinit var mFilePath: String
    private lateinit var mBitmap: Bitmap

    override fun provideViewBinding() = ActivityEditImageBinding.inflate(layoutInflater)

    private var isControllerHidden = false

    override fun initView() {
        super.initView()

        binding.headerView.setBackAction {
            finish()
        }
        mFilePath = intent.getStringExtra(IntentExtra.editImagePath)!!
        mBitmap = decodeBitmap(mFilePath)
        binding.ivImage.setImageBitmap(mBitmap)
        binding.ivImage.scaleType
        resizeViewMargin()

        binding.imagePanel.setFilterAction {
            lifecycleScope.launch(Dispatchers.IO) {
                mBitmap = decodeBitmap(mFilePath)
                when (it) {
                    FilterType.GRAY -> {
                        BitmapUtil.gray(mBitmap)
                    }
                    FilterType.AGAINST_WORLD -> {
                        BitmapUtil.againstWorld(mBitmap)
                    }
                    FilterType.ANAGLYPH -> {
                        BitmapUtil.anaglyph(mBitmap)
                    }
                    FilterType.NONE -> {}
                }
                withContext(Dispatchers.Main) {
                    binding.ivImage.setImageBitmap(mBitmap)
                    binding.paintView.setBitmap(mBitmap)
                }
            }
        }

        binding.imagePanel.setPaintAction {
            binding.paintView.setPaintColor(it)
        }

        binding.imagePanel.setEditFinishedAction {
            val bmp = saveBitmap(binding.paintView)
            binding.paintView.setBitmap(bmp)
        }
        binding.paintView.setOnChangePaintStatusAction {
            if (!isControllerHidden) {
                hideFunctionMenu()
                Log.e("PainterView", "hideFunctionMenu")
            } else {
                Log.e("PainterView", "showFunctionMenu")
                showFunctionMenu()
            }
        }
        binding.paintView.setOnHideOtherViewAction {
            Log.e("PainterView", "setOnHideOtherViewAction")
            binding.headerView.alpha = 0f
            binding.imagePanel.alpha = 0f
            isControllerHidden = true
        }

        lifecycleScope.launch(Dispatchers.Main) {
            delay(200)
            val drawable = binding.ivImage.drawable

            val matrix = binding.ivImage.imageMatrix
            val values = FloatArray(10)
            matrix.getValues(values)

            val drawWidth = drawable.bounds.width() * values[0]
            val drawHeight = drawable.bounds.height() * values[4]

            val screenWidth = getScreenWidth()
            val screenHeight = getScreenHeight()

            binding.paintView.visible()
            binding.paintView.setPaintColor(Color.BLUE)
            val layoutParam: FrameLayout.LayoutParams =
                binding.paintView.layoutParams as FrameLayout.LayoutParams
            layoutParam.width = drawWidth.toInt()
            layoutParam.height = drawHeight.toInt()
            layoutParam.topMargin = ((screenHeight - drawHeight) / 2).toInt()
            layoutParam.marginStart = ((screenWidth - drawWidth) / 2).toInt()
            binding.paintView.layoutParams = layoutParam

            val newMatrix = Matrix()
            val scaleWidth = drawWidth / mBitmap.width
            val scaleHeight = drawHeight / mBitmap.height
            newMatrix.postScale(scaleWidth, scaleHeight)
            binding.paintView.setBitmap(mBitmap)
            binding.paintView.setBitmapMatrix(newMatrix)
            binding.ivImage.invisible()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.paintView.hasEdited() || binding.imagePanel.hasEdited()) {
                    WarningDialog.show(this@EditImageActivity, 2) {
                        title("提示")
                        content("图片已修改，是否要保存?")
                        cancel("丢弃")
                        confirm("保存")
                        onCancel {
                            finish()
                        }
                        onSure {
                            //保存
                            val bmp = saveBitmap(binding.paintView)
                        }
                    }
                } else {
                    finish()
                }
            }
        })
    }

    fun saveBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        val bmp = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        view.draw(canvas)

        view.isDrawingCacheEnabled = false

        return resizeImage(bmp, mBitmap.width, mBitmap.height)
    }

    private fun resizeViewMargin() {
        if (this.checkFullScreenPhone()) {
            val topMargin = ImmersionBar.getStatusBarHeight(this)
            val headViewParam = binding.headerView.layoutParams as FrameLayout.LayoutParams
            headViewParam.topMargin = topMargin
            binding.headerView.layoutParams = headViewParam
        }
    }

    private fun resizeImage(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = w.toFloat() / width.toFloat()
        val scaleHeight = h.toFloat() / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    private fun hideFunctionMenu(duration: Long = 500) {
        binding.headerView.animate().alpha(0f).setDuration(duration)
            .withStartAction {
                binding.headerView.alpha = 1f
            }.withEndAction {
                isControllerHidden = true
            }

        binding.imagePanel.animate().alpha(0f).setDuration(duration)
            .withStartAction {
                binding.headerView.alpha = 1f
            }.withEndAction {
                isControllerHidden = true
            }
    }

    private fun showFunctionMenu(duration: Long = 500) {

        binding.headerView.animate().alpha(1f).setDuration(duration)
            .withStartAction {
                binding.headerView.alpha = 0f
            }.withEndAction {
                isControllerHidden = false
            }

        binding.imagePanel.animate().alpha(1f).setDuration(duration)
            .withStartAction {
                binding.headerView.alpha = 0f
            }.withEndAction {
                isControllerHidden = false
            }
    }

    private fun decodeBitmap(filePath: String): Bitmap {
        return if (filePath.startsWith("content://")) {
            val uri = Uri.parse(filePath)
            val ins = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(ins)
        } else {
            BitmapFactory.decodeFile(filePath)
        }
    }
}