package com.benyq.mvvm.http

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note
 */
object Setting {

    // 读超时
    const val READ_TIME_OUT = 20L
    // 写超时
    const val WRITE_TIME_OUT = 30L
    // 连接超时
    const val CONNECT_TIME_OUT = 5L

    // 请求成功状态码
    const val SUCCESS = 0

    const val UNKNOWN_ERROR = "未知异常"

    const val MESSAGE_EMPTY = "message is null"

    const val DATA_EMPTY = "data is null"

}