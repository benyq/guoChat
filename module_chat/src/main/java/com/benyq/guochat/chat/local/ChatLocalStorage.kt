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

    var userAccount by MMKVValue("userAccount", UserBean("klfjjasjasjda", "yzj123", "苏打先生", "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fp4.itc.cn%2Fq_70%2Fimages03%2F20201126%2F0e642a81f3744912ae8836623487871f.jpeg&refer=http%3A%2F%2Fp4.itc.cn&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1612666237&t=7672741f9120fe459b3ac417a594287d", "","15858129939", 1))

    fun updateUserAccount(action: UserBean.()->Unit){
        val userBean = userAccount
        action.invoke(userBean)
        userAccount = userBean
    }
}