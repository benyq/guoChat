package com.benyq.guochat.wanandroid.model.vm

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.wanandroid.model.PersonScoreData
import com.benyq.guochat.wanandroid.model.UserData
import com.benyq.guochat.wanandroid.LocalCache
import com.benyq.guochat.wanandroid.model.repository.MeRepository
import com.benyq.module_base.mvvm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @author benyq
 * @time 2021/8/5
 * @e-mail 1520063035@qq.com
 * @note
 */
@HiltViewModel
class MeViewModel @Inject constructor(private val repository: MeRepository) : BaseViewModel(){

    val userData = MutableLiveData<UserData>()

    init {
        LocalCache.loginData.run {
            if (!isEmpty()) {
                userData.value = UserData(username, id, coinCount, -1)
            }
        }
    }

    fun getCoinCount() {
        quickLaunch<PersonScoreData> {
            onSuccess {
                val loginData = LocalCache.loginData
                userData.value = UserData(loginData.username, loginData.id, it!!.coinCount, it.rank)
            }
            request {
                repository.getCoinCount()
            }
        }
    }
}