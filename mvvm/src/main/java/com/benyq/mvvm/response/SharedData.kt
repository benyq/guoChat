package com.benyq.mvvm.response

import com.benyq.mvvm.R

/**
 * @author benyq
 * @time 2020/4/7
 * @e-mail 1520063035@qq.com
 * @note liveData 传递数据，通信使用
 */
data class SharedData(val msg: String = "",
                      val strRes: Int = 0,
                      val throwable: Throwable = Throwable(),
                      val type: SharedType = SharedType.RESOURCE)