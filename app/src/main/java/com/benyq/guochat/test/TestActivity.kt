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
    }

    override fun initListener() {

    }



}