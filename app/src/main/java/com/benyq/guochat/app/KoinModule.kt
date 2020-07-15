package com.benyq.guochat.app

import com.benyq.guochat.model.net.ApiService
import com.benyq.guochat.model.net.ServiceFactory
import com.benyq.guochat.model.rep.*
import com.benyq.guochat.model.vm.*
import com.benyq.mvvm.http.RetrofitFactory
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * @author benyq
 * @time 2020/7/9
 * @e-mail 1520063035@qq.com
 * @note
 */

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { MainViewModel(get()) }
    viewModel { ContractsViewModel(get()) }
    viewModel { ChatViewModel(get()) }
    viewModel { ChatDetailViewModel(get()) }
    viewModel { PersonalInfoViewModel(get()) }
    viewModel { PictureVideoViewModel() }
    viewModel { FriendCircleViewModel(get()) }
}

val repositoryModule = module {
    single {
        ServiceFactory.initClient()
        RetrofitFactory.create(ApiService::class.java)
    }
    single { LoginRepository() }
    single { MainRepository() }
    single { ContractsRepository() }
    single { ChatRepository() }
    single { ChatDetailRepository() }
    single { UserInfoRepository() }
    single { FriendCircleRepository() }
}

val appModule = listOf(viewModelModule, repositoryModule)