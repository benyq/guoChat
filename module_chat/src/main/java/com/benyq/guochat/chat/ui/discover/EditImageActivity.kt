package com.benyq.guochat.chat.ui.discover

import android.graphics.*
import android.view.View
import android.widget.FrameLayout
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
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

    override fun initView() {
        super.initView()

        binding.headerView.setBackAction {
            finish()
        }
        mFilePath = intent.getStringExtra(IntentExtra.editImagePath)!!
        mBitmap = BitmapFactory.decodeFile(mFilePath)
        binding.ivImage.setImageBitmap(mBitmap)
        binding.ivImage.scaleType
        resizeViewMargin()

        binding.imagePanel.setFilterAction{
            lifecycleScope.launch(Dispatchers.IO) {
                mBitmap = BitmapFactory.decodeFile(mFilePath)
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

        lifecycleScope.launch(Dispatchers.Main) {
            delay(200)
            val drawable = binding.ivImage.drawable

            val matrix = binding.ivImage.imageMatrix
            val values = FloatArray(10)
            matrix.getValues(values)

            val drawWidth = drawable.bounds.width()*values[0]
            val drawHeight = drawable.bounds.height()*values[4]

            val screenWidth = getScreenWidth()
            val screenHeight = getScreenHeight()

            binding.paintView.visible()
            binding.paintView.setPaintColor(Color.BLUE)
            val layoutParam: FrameLayout.LayoutParams = binding.paintView.layoutParams as FrameLayout.LayoutParams
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

        onBackPressedDispatcher.addCallback(this, object :  OnBackPressedCallback(true){
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
                }else {
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
            val topMargin = (this.dip2px(15) + ImmersionBar.getStatusBarHeight(this)).toInt()
            val headViewParam = binding.headerView.layoutParams as ConstraintLayout.LayoutParams
            headViewParam.topMargin = topMargin
            binding.headerView.layoutParams = headViewParam
        }
    }

    fun resizeImage(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = w.toFloat() / width.toFloat()
        val scaleHeight = h.toFloat() / height.toFloat()
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

}