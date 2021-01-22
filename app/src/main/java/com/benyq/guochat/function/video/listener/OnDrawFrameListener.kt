package com.benyq.guochat.function.video.listener

/**
 * @author benyqYe
 * date 2021/1/22
 * e-mail 1520063035@qq.com
 * description 给视频录制提供的接口
 */

interface OnDrawFrameListener {

    fun onDrawFrame(
        cameraTexId: Int,
        cameraWidth: Int,
        cameraHeight: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?,
        timeStamp: Long
    ) {
    }

}