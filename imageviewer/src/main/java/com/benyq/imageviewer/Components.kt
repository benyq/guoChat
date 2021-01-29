package com.benyq.imageviewer

import android.view.View
import androidx.lifecycle.MutableLiveData

internal object Components {

    var isFullScreen = true
    var data: MutableList<PreviewPhoto> = mutableListOf()
    var cacheView: MutableList<View> = mutableListOf()
    var curPosition: Int = -1





    fun release() {
        isFullScreen = true
        data.clear()
        cacheView.clear()
        curPosition = -1
    }
}