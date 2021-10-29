package com.benyq.guochat.chat.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.chat.model.rep.UserInfoRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

/**
 * @author benyq
 * @time 2020/6/4
 * @e-mail 1520063035@qq.com
 * @note 修改个人信息的ViewModel， 几个viewModelOwner公用
 */
@HiltViewModel
class PersonalInfoViewModel @Inject constructor(private val mRepository: UserInfoRepository) :
    BaseViewModel() {

    val editNickLiveData = MutableLiveData<String>()
    val uploadAvatarLiveData = MutableLiveData<String>()

    fun editUserNick(nick: String) {
        quickLaunch<String> {
            onStart { showLoading("") }
            onSuccess { editNickLiveData.value = it }
            request { mRepository.editUserNick(nick) }
            onFinal { hideLoading() }
        }
    }

    fun uploadAvatar(file: File) {
        quickLaunch<String> {
            onStart { showLoading("正在上传") }
            onFinal { hideLoading() }
            onSuccess { uploadAvatarLiveData.value = it }
            request { mRepository.uploadAvatar(file) }
        }
    }
}