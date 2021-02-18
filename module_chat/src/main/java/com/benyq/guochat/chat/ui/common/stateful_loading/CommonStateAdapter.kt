package com.benyq.guochat.chat.ui.common.stateful_loading

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.benyq.guochat.chat.R

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
                holder.getView<TextView>(R.id.btn_reload).setOnClickListener {
                    holder.onReloadListener?.invoke(it)
                }
            }
        }
    }


}