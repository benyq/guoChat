package com.benyq.guochat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.rep.UserInfoRepository
import com.benyq.mvvm.mvvm.BaseViewModel
import java.io.File

/**
 * @author benyq
 * @time 2020/6/4
 * @e-mail 1520063035@qq.com
 * @note 修改个人信息的ViewModel， 几个viewModelOwner公用
 */
class PersonalInfoViewModel : BaseViewModel<UserInfoRepository>(){

    val editNickLiveData = MutableLiveData<String>()
    val uploadAvatarLiveData = MutableLiveData<String>()

    fun editUserNick(nick: String){
        quickLaunch<String> {
            onSuccess { editNickLiveData.value = it }
            request { mRepository.editUserNick(nick) }
        }
    }

    fun uploadAvatar(file: File){
        quickLaunch<String> {
            onStart { showLoading("正在上传") }
            onFinal { hideLoading() }
            onSuccess { uploadAvatarLiveData.value = it }
            request { mRepository.uploadAvatar(file) }
        }
    }
}