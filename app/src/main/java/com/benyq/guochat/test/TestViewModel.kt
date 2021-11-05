package com.benyq.guochat.test

import androidx.lifecycle.MutableLiveData
import com.benyq.module_base.ext.loge
import com.benyq.module_base.mvvm.BaseViewModel
import kotlinx.coroutines.flow.*

class TestViewModel : BaseViewModel() {

    val result by lazy { MutableLiveData<String>() }


    fun test() {

    }


}



