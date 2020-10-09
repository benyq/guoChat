package com.benyq.guochat.comic.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
class ComicViewModel @ViewModelInject constructor(): BaseViewModel(){

    /**
     * Activity中页面下标
     */
    var mCurrentIndex = 0
}