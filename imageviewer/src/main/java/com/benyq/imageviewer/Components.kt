package com.benyq.imageviewer

import android.view.View
import androidx.core.view.children
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

internal object Components {

    var isFullScreen = true
    var data: MutableList<PreviewPhoto> = mutableListOf()
    var recyclerview: RecyclerView? = null
    var cacheView: MutableList<View?> = mutableListOf()
    var curPosition: Int = -1
    var thumbnailViewId: Int = -1

    var isLoad: Boolean  = false

    fun getView(position: Int): View?{
        if (recyclerview != null) {
            return getViewInRecycler(position)?.findViewById(thumbnailViewId)
        }
        if (cacheView.size > position) {
            return cacheView[position]
        }
        return null
    }

    fun release() {
        isFullScreen = true
        data.clear()
        cacheView.clear()
        curPosition = -1
        thumbnailViewId = -1
        isLoad = false
    }


    private fun getViewInRecycler(position: Int): View? {
        val layoutManager = recyclerview?.layoutManager

        var pos = position
        when (layoutManager) {
            is GridLayoutManager -> {
                pos = getRecyclerViewId(layoutManager, pos)
            }
            is LinearLayoutManager -> {
                pos = getRecyclerViewId(layoutManager, pos)
            }
            is StaggeredGridLayoutManager -> {
            }
        }
        return recyclerview?.getChildAt(pos) ?: return null
    }


    //获取recyclerView Id
    private fun getRecyclerViewId(
        layoutManager: LinearLayoutManager,
        pos: Int
    ): Int {
        var pos1 = pos
        pos1 -= layoutManager.findFirstVisibleItemPosition()
        return pos1
    }
}