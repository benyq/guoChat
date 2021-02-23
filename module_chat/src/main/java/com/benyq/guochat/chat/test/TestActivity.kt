package com.benyq.guochat.chat.test

import android.os.Environment
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.Singleton
import com.benyq.guochat.chat.databinding.ActivityTestBinding
import com.benyq.guochat.chat.ui.chats.video.PictureVideoActivity
import com.benyq.imageviewer.ImagePreview
import com.benyq.imageviewer.PreviewPhoto
import com.benyq.imageviewer.PreviewTypeEnum
import com.benyq.module_base.ext.goToActivity
import com.benyq.module_base.ext.loadImage
import com.benyq.module_base.ui.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TestActivity : BaseActivity<ActivityTestBinding>() {

    private val TAG = "TestActivity"
    private lateinit var mViewModel: TestViewModel

    override fun provideViewBinding() = ActivityTestBinding.inflate(layoutInflater)

    override fun initView() {
        mViewModel = ViewModelProvider(this).get(TestViewModel::class.java)
    }

    override fun initListener() {

    }



}