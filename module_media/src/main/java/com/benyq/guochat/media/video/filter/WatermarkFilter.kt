package com.benyq.guochat.media.video.filter

import com.benyq.guochat.media.video.drawer.BaseDrawer

/**
 * @author benyq
 * @time 2021/3/27
 * @e-mail 1520063035@qq.com
 * @note  处理水印
 */
class WatermarkFilter(private val drawer: BaseDrawer): NoFilter(true){

    override fun onDraw(textureId: Int) {
        super.onDraw(textureId)
        //不需要，自身包含矩阵
        drawer.drawFrame(0, null, null)
    }

    override fun release() {
        super.release()
        drawer.release()
    }

}