package com.benyq.guochat.function.video.texture

import android.opengl.GLES20

/**
 * author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description 纹理类基类
 */

open class BaseTexture {

    var textureId = 0

    open fun release() {
        GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
    }
}