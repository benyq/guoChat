package com.benyq.guochat.test

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.R
import com.benyq.guochat.core.BitmapUtil
import com.benyq.guochat.databinding.ActivityTestBinding
import com.benyq.module_base.ext.loge
import com.benyq.module_base.ui.base.BaseActivity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TestActivity : BaseActivity<ActivityTestBinding>() {

    private val TAG = "TestActivity"

    private lateinit var mViewModel: TestViewModel

    override fun isHideBar() = true

    override fun provideViewBinding() = ActivityTestBinding.inflate(layoutInflater)

    override fun initView() {
        mViewModel = ViewModelProvider(this).get(TestViewModel::class.java)

        binding.ivTest.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.attack_on_titan))

        binding.btnGray.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.attack_on_titan)
            BitmapUtil.gray(bitmap)
            binding.ivTest.setImageBitmap(bitmap)
        }
        binding.btnAgainstWorld.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.attack_on_titan)
            BitmapUtil.againstWorld(bitmap)
            binding.ivTest.setImageBitmap(bitmap)
        }
        binding.btnAnaglyph.setOnClickListener {
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.attack_on_titan)
            BitmapUtil.anaglyph(bitmap)
            binding.ivTest.setImageBitmap(bitmap)
        }
        binding.sbBrightness.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.attack_on_titan)
                BitmapUtil.brightness(bitmap, (progress - 128).toFloat())
                binding.ivTest.setImageBitmap(bitmap)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    override fun initListener() {

    }



}