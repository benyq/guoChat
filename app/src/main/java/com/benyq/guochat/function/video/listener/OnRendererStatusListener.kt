package com.benyq.guochat.function.video.listener

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

    /**
     * Called when camera changed
     *
     * @param cameraFacing      FACE_BACK = 0, FACE_FRONT = 1
     * @param cameraOrientation
     */
    fun onCameraChanged(cameraFacing: Int, cameraOrientation: Int) {}

}