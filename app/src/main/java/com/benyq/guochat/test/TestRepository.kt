package com.benyq.guochat.test

import com.benyq.guochat.chat.model.bean.ChatResponse
import com.benyq.guochat.chat.model.net.ChatApiService
import com.benyq.guochat.chat.model.net.ChatServiceFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class TestRepository @Inject constructor(){

    private val apiService: ChatApiService = ChatServiceFactory.provideApiService()

    suspend fun test(): Flow<ChatResponse<Boolean>> {
        return flow {
            delay(200)
            emit(ChatResponse.success(true))
        }
    }

    fun test2(): Flow<ChatResponse<String>> {

            val start = System.currentTimeMillis()

            val flowA = flow {
                delay(1400)
                emit(3)
            }
            val flowB = flow {
                delay(3600)
                emit(6)
            }
            println("before ${System.currentTimeMillis() - start}")
            return flowA.zip(flowB) { i: Int, i2: Int ->
                println("transform ${System.currentTimeMillis() - start}")
                ChatResponse.success("${i * i2}")
            }
    }

    fun test3() : Flow<ChatResponse<Int>> {
        return flow {
            delay(1600)
            emit(ChatResponse.success(22))
        }
    }

    fun test4() : Flow<ChatResponse<Int>> {
        return flow {
            delay(3600)
            emit(ChatResponse.success(11))
        }
    }

    fun test5() : Flow<ChatResponse<Int>> {
        return flow {
            delay(2600)
            emit(ChatResponse.success(33))
        }
    }

}

