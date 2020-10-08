package com.benyq.guochat.comic.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * @author benyq
 * @time 2020/9/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class HomeSortItemDecoration(val space: Int, val headSpace: Int, val tailSpace: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        when (parent.getChildAdapterPosition(view)) {
            0 -> {
                outRect.left = headSpace
                outRect.right = space / 2
            }
            state.itemCount - 1 -> {
                outRect.left = space / 2
                outRect.right = tailSpace
            }
            else -> {
                outRect.left = space / 2
                outRect.right = space / 2
            }
        }
    }

}