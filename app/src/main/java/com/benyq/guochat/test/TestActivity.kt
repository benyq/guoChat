package com.benyq.guochat.test

import androidx.lifecycle.ViewModelProvider
import com.benyq.guochat.databinding.ActivityTestBinding
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
        val imgPath = intent.getStringExtra("imgPath")
        Glide.with(this).load(imgPath).into(binding.ivTest)
    }

    override fun initListener() {

    }



}