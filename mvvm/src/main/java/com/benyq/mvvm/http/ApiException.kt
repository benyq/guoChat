package com.benyq.mvvm.http

/**
 * @author benyq
 * @time 2020/9/13
 * @e-mail 1520063035@qq.com
 * @note
 */
class ApiException(val code: Int, val msg: String) : RuntimeException(msg)