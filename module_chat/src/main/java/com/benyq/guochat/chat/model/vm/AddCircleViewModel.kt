package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.model.rep.AddCircleRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/7/23
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class AddCircleViewModel @Inject constructor(private val mRepository: AddCircleRepository) :
    BaseViewModel() {
    val addCirclePhotoUrlData: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun addCirclePhotoUrl(url: List<String>) {
        val urls = addCirclePhotoUrlData.value ?: mutableListOf()
        urls.addAll(url)
        addCirclePhotoUrlData.value = urls
    }
}