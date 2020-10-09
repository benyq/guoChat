package com.benyq.guochat.comic.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author benyq
 * @time 2020/9/24
 * @e-mail 1520063035@qq.com
 * @note 配合 GridLayoutManager 使用, 每一行都会有间隔
 */
class GridItemDecoration(private val topSpace: Int, private val bottomSpace: Int, space: Int= 0) : RecyclerView.ItemDecoration(){
    private var space: Int = 0

    init {
        this.space = space / 2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition < layoutManager.spanCount) {
                outRect.top = topSpace
                outRect.bottom = bottomSpace
            }else{
                outRect.bottom = bottomSpace
            }
            when {
                itemPosition % layoutManager.spanCount == 0 -> {
                    outRect.left = 0
                    outRect.right = space
                }
                (itemPosition + 1) % layoutManager.spanCount == 0 -> {
                    outRect.left = space
                    outRect.right = 0
                }
                else -> {
                    outRect.left = space
                    outRect.right = space
                }
            }
        }

    }
}