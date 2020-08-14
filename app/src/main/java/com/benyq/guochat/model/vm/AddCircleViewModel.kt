package com.benyq.guochat.model.vm

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.rep.AddCircleRepository
import com.benyq.mvvm.mvvm.BaseViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/7/23
 * @e-mail 1520063035@qq.com
 * @note
 */
class AddCircleViewModel @ViewModelInject constructor(private val mRepository: AddCircleRepository) : BaseViewModel(){
    val addCirclePhotoUrlData : MutableLiveData<MutableList<String>> = MutableLiveData()

    fun addCirclePhotoUrl(url: List<String>) {
        val urls = addCirclePhotoUrlData.value ?: mutableListOf()
        urls.addAll(url)
        addCirclePhotoUrlData.value = urls
    }
}