package com.benyq.module_base.ui.widget

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import com.benyq.module_base.R
import com.benyq.module_base.ext.Toasts
import com.benyq.module_base.ext.loge
import kotlinx.android.synthetic.main.view_picture_puzzle.view.*
import java.math.RoundingMode


/**
 * @author benyq
 * @time 2020/10/18
 * @e-mail 1520063035@qq.com
 * @note 拼图控件
 */

typealias PuzzleResultAction = (Boolean, Float)->Unit

class PicturePuzzleView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    LinearLayout(context, attrs, defStyleAttr) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    private var mResultAction: PuzzleResultAction ? = null
    private var mTouchTime = 0L
    private var imgLoaded = false

    init {
        View.inflate(context, R.layout.view_picture_puzzle, this)

        sbProgress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pImage.setMaskRadio(progress.toFloat() / sbProgress.max)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mTouchTime = System.currentTimeMillis()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //判断是否正确
                val result = pImage.checkResult()
                val time = (System.currentTimeMillis() - mTouchTime) / 1000f
                if (!result) {
                    sbProgress.progress = 0
                }
                mResultAction?.invoke(result, time.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toFloat())
            }

        })
    }

    fun setBitmap(bitmap: Bitmap) {
        pImage.setImageBitmap(bitmap)
        imgLoaded = pImage.prepare()
    }

    fun setResultAction(action: PuzzleResultAction) {
        mResultAction = action
    }
}
