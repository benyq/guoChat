package com.benyq.guochat.media.video

import android.graphics.Bitmap

//GLSurfaceView渲染监听
interface OnRendererStatusListener {
    /**
     * Called when surface is created or recreated.
     */
    fun onSurfaceCreated() {}

    /**
     * Called when surface'size changed.
     *
     * @param viewWidth
     * @param viewHeight
     */
    fun onSurfaceChanged(viewWidth: Int, viewHeight: Int) {}

    /**
     * Called when drawing current frame
     *
     * @param cameraTexId
     * @param mvpMatrix
     * @param texMatrix
     * @return
     */
    fun onDrawFrame(
        cameraTexId: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?,
    ) {
    }
}

//给视频录制提供的接口
interface OnDrawFrameListener {

    fun onDrawFrame(
        cameraTexId: Int,
        cameraWidth: Int,
        cameraHeight: Int,
        mvpMatrix: FloatArray?,
        texMatrix: FloatArray?,
        timeStamp: Long
    )
}
