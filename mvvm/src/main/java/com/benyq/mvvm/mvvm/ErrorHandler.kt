package com.benyq.mvvm.mvvm

/**
 * @author benyq
 * @time 2020/9/13
 * @e-mail 1520063035@qq.com
 * @note 全局注册网络请求异常处理
 */
object ErrorHandler {

    var errorBlock: ((Throwable) -> Unit)? = null
    private set

    fun injectHandler(block: (Throwable) -> Unit){
        errorBlock = block
    }


}

/**
 * 请求网络失败原因
 */
enum class ExceptionReason {
    /**
     * 解析数据失败
     */
    PARSE_ERROR,
    /**
     * 网络问题
     */
    BAD_NETWORK,
    /**
     * 连接错误
     */
    CONNECT_ERROR,
    /**
     * 连接超时
     */
    CONNECT_TIMEOUT,
    /**
     * 未知错误
     */
    UNKNOWN_ERROR
}