package com.benyq.guochat.function.media.opengl;

/**
 * @author Richie on 2019.08.23
 */
public interface OnRendererStatusListener {
    /**
     * Called when surface is created or recreated.
     */
    default void onSurfaceCreated(){}

    /**
     * Called when surface'size changed.
     *
     * @param viewWidth
     * @param viewHeight
     */
    default void onSurfaceChanged(int viewWidth, int viewHeight){}

    /**
     * Called when drawing current frame
     *
     * @param cameraNv21Byte
     * @param cameraTexId
     * @param cameraWidth
     * @param cameraHeight
     * @param mvpMatrix
     * @param texMatrix
     * @param timeStamp
     * @return
     */
    default void onDrawFrame(byte[] cameraNv21Byte, int cameraTexId, int cameraWidth, int cameraHeight, float[] mvpMatrix, float[] texMatrix, long timeStamp){}

    /**
     * Called when camera changed
     *
     * @param cameraFacing      FACE_BACK = 0, FACE_FRONT = 1
     * @param cameraOrientation
     */
    default void onCameraChanged(int cameraFacing, int cameraOrientation){}
}
