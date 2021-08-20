package com.benyq.guochat.comic.model.vm

import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class ComicViewModel @Inject constructor() : BaseViewModel() {

    /**
     * Activity中页面下标
     */
    var mCurrentIndex = 0
}