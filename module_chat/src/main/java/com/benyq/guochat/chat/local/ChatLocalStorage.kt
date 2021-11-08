package com.benyq.guochat.chat.local

import com.benyq.guochat.chat.model.bean.PersonConfig
import com.benyq.guochat.chat.model.bean.UserBean
import com.benyq.module_base.MMKVValue

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 本地缓存信息管理
 */
object ChatLocalStorage {

    var token by MMKVValue("token", "")

    var uid by MMKVValue("uid", "")

    var phoneNumber by MMKVValue("phoneNumber", "")

    var personConfig by MMKVValue("personConfig", PersonConfig.defaultConfig())

    var userAccount by MMKVValue("userAccount", UserBean.empty())

    fun updateUserAccount(action: UserBean.() -> Unit) {
        val userBean = userAccount
        action.invoke(userBean)
        userAccount = userBean
    }

    fun logout() {
        token = ""
        uid = ""
        phoneNumber = ""
        userAccount = UserBean.empty()
    }

    fun existUser(): Boolean {
        return token.isNotEmpty() && uid.isNotEmpty()
    }
}