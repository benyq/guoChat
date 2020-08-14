package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import com.benyq.guochat.model.rep.MainRepository
import com.benyq.mvvm.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MainViewModel @ViewModelInject constructor(private val mRepository: MainRepository) : BaseViewModel(){

    /**
     * MainActivity中页面下标
     */
    var mCurrentIndex = 0


}