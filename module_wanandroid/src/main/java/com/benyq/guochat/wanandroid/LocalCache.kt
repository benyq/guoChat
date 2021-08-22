package com.benyq.guochat.wanandroid

import com.benyq.guochat.wanandroid.model.LoginData
import com.benyq.module_base.MMKVValue

/**
 * @author benyq
 * @date 2021/8/5
 * @email 1520063035@qq.com
 */

object LocalCache {
    var loginData by MMKVValue("loginData", LoginData.empty())
}
