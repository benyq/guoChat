package com.benyq.imageviewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author benyqYe
 * date 2021/2/1
 * e-mail 1520063035@qq.com
 * description 传递一些参数
 */

class PreviewViewModel : ViewModel(){

    val viewerUserInputEnabled = MutableLiveData<Boolean>()

    val exitAnimPosition = MutableLiveData<Int>()

    var isExiting = false

    fun setViewerUserInputEnabled(enable: Boolean) {
        if (viewerUserInputEnabled.value != enable) viewerUserInputEnabled.value = enable
    }

    fun setExitAnimPosition(position: Int) {
        exitAnimPosition.value = position
    }
}