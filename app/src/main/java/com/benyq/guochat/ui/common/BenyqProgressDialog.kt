package com.benyq.guochat.ui.common

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.benyq.guochat.R
import com.benyq.guochat.ui.base.DrawableBuilder
import com.benyq.mvvm.ext.gone
import com.benyq.mvvm.ext.loge
import kotlinx.android.synthetic.main.progress_dialog.*

/**
 * @author benyq
 * @emil 1520063035@qq.com
 * create at 2020/2/4
 * description:
 */
class BenyqProgressDialog(context: Context, val content: String? = "") : AlertDialog(context, R.style.dialog_style){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.progress_dialog)
        llContainer.background = DrawableBuilder(context)
            .fill("#202020")
            .corner(5f)
            .build()
        if (content.isNullOrEmpty()) {
            tvLoading.gone()
        }else {
            tvLoading.text = content
        }

    }
}