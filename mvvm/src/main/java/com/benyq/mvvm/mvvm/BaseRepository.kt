package com.benyq.mvvm.mvvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
abstract class BaseRepository {

    suspend fun <R> launchIO(block: suspend CoroutineScope.() -> R) = withContext(Dispatchers.IO) {
        block()
    }

}