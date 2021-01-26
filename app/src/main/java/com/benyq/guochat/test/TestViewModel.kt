package com.benyq.guochat.test

import androidx.lifecycle.MutableLiveData
import com.benyq.guochat.model.bean.ChatResponse
import com.benyq.mvvm.ext.loge
import com.benyq.mvvm.mvvm.BaseViewModel
import kotlinx.coroutines.flow.*

class TestViewModel : BaseViewModel() {

    val result by lazy { MutableLiveData<String>() }

    private val mRepository: TestRepository = TestRepository()

    fun test() {

        quickLaunch<Boolean> {

            onStart {
                loge("flow test onStart")
            }

            onError {
                loge("flow test onError $it")
            }

            onFinal {
                loge("flow test onFinal")
            }

            onSuccess {
                loge("flow test success $it")
                result.value = it.toString()
            }

            requestFlow {
                mRepository.test3().zip(mRepository.test4()){ i,i2 ->
                    ChatResponse.success(true)
                }
            }
        }
    }


}



