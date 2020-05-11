package com.benyq.guochat.local

import com.benyq.guochat.local.MMKVValue


object PreferenceManager {

    var token by MMKVValue("token", "")

    var shopId by MMKVValue("shopId", "")

    var marketId by MMKVValue("marketId", "")

    /**
     * 管理员id
     */
    var uid by MMKVValue("uid", "")

    /**
     * 平台用户1，0其他用户
     */
    var platformUser by MMKVValue("platformUser", 0)

    var password by MMKVValue("password", "")

    var account by MMKVValue("account", "")

}