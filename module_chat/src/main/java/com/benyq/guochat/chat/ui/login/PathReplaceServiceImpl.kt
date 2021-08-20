package com.benyq.guochat.chat.ui.login

import android.content.Context
import android.net.Uri
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.PathReplaceService
import com.benyq.guochat.chat.function.fingerprint.FingerprintVerifyManager
import com.benyq.guochat.chat.local.ChatLocalStorage
import com.benyq.module_base.RouterPath
import com.benyq.module_base.ext.loge

/**
 * @author benyq
 * @time 2021/4/4
 * @e-mail 1520063035@qq.com
 * @note 这个 PathReplaceService 全局只有一个会生效
 * 不过别的地方也没有用到，所以暂时就只有登录这个有用
 */
@Route(path = RouterPath.CHAT_PATH_REPLACE)
class PathReplaceServiceImpl : PathReplaceService{

    private lateinit var mContext: Context

    override fun init(context: Context) {
        mContext = context
    }

    override fun forString(path: String): String {
        loge("arouterforString path: $path")
        if (path.contains("login")) {
            if (FingerprintVerifyManager.canAuthenticate(mContext) && ChatLocalStorage.personConfig.fingerprintLogin) {
                return RouterPath.CHAT_LOGIN_FINGER
            }
            return RouterPath.CHAT_LOGIN_PWD
        }
        return path
    }

    override fun forUri(uri: Uri): Uri {
        return uri
    }
}