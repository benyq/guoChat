package com.benyq.guochat.local

import com.benyq.guochat.model.bean.PersonConfig
import com.benyq.guochat.model.bean.UserBean

/**
 * @author benyq
 * @time 2020/5/20
 * @e-mail 1520063035@qq.com
 * @note 本地缓存信息管理
 */
object LocalStorage {

    var token by MMKVValue("authToken", "")

    var uid by MMKVValue("uid", "")

    var phoneNumber by MMKVValue("phoneNumber", "")

    var personConfig by MMKVValue("personConfig", PersonConfig.defaultConfig())

    var userAccount by MMKVValue("userAccount", UserBean("klfjjasjasjda", "yzj123", "苏打先生", "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1368451564,780267377&fm=111&gp=0.jpg"))

    fun updateUserAccount(action: UserBean.()->Unit){
        val userBean = userAccount
        action.invoke(userBean)
        userAccount = userBean
    }
}