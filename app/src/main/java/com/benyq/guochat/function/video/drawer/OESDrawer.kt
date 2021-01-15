package com.benyq.guochat.function.video.drawer

import com.benyq.guochat.function.video.OpenGLTools
import com.benyq.guochat.function.video.VideoFormat
import com.benyq.guochat.function.video.filter.BaseFilter
import com.benyq.guochat.function.video.filter.OESFilter
import com.benyq.guochat.function.video.texture.BaseTexture
import com.benyq.guochat.function.video.texture.VideoTexture

/**
 * @author benyqYe
 * date 2021/1/15
 * e-mail 1520063035@qq.com
 * description OES
 */

class OESDrawer(texture: BaseTexture) : BaseDrawer(){

    init {
        mTexture = texture
        mFilter.updateTexture(mTexture.textureId)
    }

    override fun getVertexCoors() = floatArrayOf(
        -1f, -1f,
        1f, -1f,
        -1f, 1f,
        1f, 1f
    )

    override fun getTextureCoors() = floatArrayOf(
        0f, 0f,
        1f, 0f,
        0f, 1f,
        1f, 1f
    )

    override fun draw() {
        mFilter.draw()
    }

    override fun release() {

    }
}