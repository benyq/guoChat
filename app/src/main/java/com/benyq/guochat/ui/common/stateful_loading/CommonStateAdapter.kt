package com.benyq.guochat.ui.common.stateful_loading

import android.view.LayoutInflater
import android.view.ViewGroup
import com.benyq.guochat.R
import com.benyq.mvvm.ext.Toasts
import kotlinx.android.synthetic.main.layout_error.view.*

/**
 * @author benyq
 * @time 2020/7/17
 * @e-mail 1520063035@qq.com
 * @note
 */
class CommonStateAdapter : Adapter<ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, type: ViewType): ViewHolder {
        val layoutId =  when(type) {
            ViewType.LOADING -> R.layout.layout_loading
            ViewType.ERROR -> R.layout.layout_error
            else -> R.layout.layout_loading
        }
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder) {
        when(holder.viewType) {
            ViewType.LOADING -> {

            }
            ViewType.CONTENT -> {

            }
            ViewType.ERROR -> {
                holder.rootView.btn_reload.setOnClickListener {
                    holder.onReloadListener?.invoke(it)
                }
            }
        }
    }


}