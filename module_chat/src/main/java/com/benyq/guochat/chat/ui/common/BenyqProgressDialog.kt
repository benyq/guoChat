package com.benyq.guochat.chat.ui.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.benyq.guochat.chat.R
import com.benyq.guochat.chat.databinding.ProgressDialogBinding
import com.benyq.module_base.DrawableBuilder
import com.benyq.module_base.ext.gone

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/2/4
 * description:
 */
class BenyqProgressDialog(context: Context, val content: String? = "") : AlertDialog(context, R.style.dialog_style){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ProgressDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.llContainer.background = DrawableBuilder(context)
            .fill("#202020")
            .corner(5f)
            .build()
        if (content.isNullOrEmpty()) {
            binding.tvLoading.gone()
        }else {
            binding.tvLoading.text = content
        }

    }
}