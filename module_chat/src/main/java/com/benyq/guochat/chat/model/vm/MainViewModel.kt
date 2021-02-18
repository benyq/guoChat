package com.benyq.guochat.chat.model.vm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.benyq.guochat.chat.model.rep.MainRepository
import com.benyq.module_base.mvvm.BaseViewModel

/**
 * @author benyq
 * @time 2020/4/21
 * @e-mail 1520063035@qq.com
 * @note
 */
class MainViewModel @ViewModelInject constructor(
    @Assisted private val handle: SavedStateHandle,
    private val mRepository: MainRepository
) : BaseViewModel() {

    /**
     * MainActivity中页面下标
     */
    var mCurrentIndex: Int
        set(value) {
            handle.set("ds", value)
        }
        get() {
            return handle.get<Int>("ds") ?: 0
        }

    init {
        if (!handle.contains("ds")) {
            handle.set("ds", 0)
        }
    }

}