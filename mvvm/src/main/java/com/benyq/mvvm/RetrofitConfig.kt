package com.benyq.mvvm

import okhttp3.MediaType.Companion.toMediaTypeOrNull

/**
 * @author benyq
 * @time 2020/9/20
 * @e-mail 1520063035@qq.com
 * @note
 */

val TEXT = "text/plain; charset=utf-8".toMediaTypeOrNull()
val STREAM = "application/octet-stream".toMediaTypeOrNull()
val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
val FormUrlEncoded = "application/x-www-form-urlencoded;charset=UTF-8".toMediaTypeOrNull()