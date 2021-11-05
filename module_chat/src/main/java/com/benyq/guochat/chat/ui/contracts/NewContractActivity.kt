package com.benyq.guochat.chat.ui.contracts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.benyq.guochat.chat.databinding.ActivityNewContractBinding
import com.benyq.module_base.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author benyq
 * @date 2021/11/4
 * @email 1520063035@qq.com
 * 新的朋友界面
 */
@AndroidEntryPoint
class NewContractActivity : BaseActivity<ActivityNewContractBinding>() {
    override fun provideViewBinding(): ActivityNewContractBinding = ActivityNewContractBinding.inflate(layoutInflater)


}