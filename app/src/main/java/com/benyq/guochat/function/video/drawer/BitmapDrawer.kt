package com.benyq.guochat.function.video.drawer

class BitmapDrawer : BaseDrawer() {

    //纹理ID
    private var mTextureId: Int = -1
    //program ID
    private var mProgram = -1

    // 顶点坐标接收者
    private var mVertexPosHandle: Int = -1
    // 纹理坐标接收者
    private var mTexturePosHandle: Int = -1
    // 纹理接收者
    private var mTextureHandle: Int = -1

    override fun getVertexCoors() = floatArrayOf(
        -0.5f, -0.5f,
        0.5f, -0.5f,
        -0.5f, 0.5f,
        0.5f, 0.5f
    )

    override fun getTextureCoors() = floatArrayOf(
        0.25f, 0.75f,
        0.75f, 0.75f,
        0.25f, 0.25f,
        0.75f, 0.25f
    )



    override fun draw() {

    }

    override fun release() {

    }
}