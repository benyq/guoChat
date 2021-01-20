package com.benyq.guochat.function.video.filter

import android.opengl.GLES20

/**
 * @author benyqYe
 * date 2021/1/19
 * e-mail 1520063035@qq.com
 * description 所有滤镜都处理好之后，直接渲染的滤镜
 */

class NoFilter : BaseFilter(){

    init {
        enableFrameBuffer = false
    }

    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

    }

    override fun getVertexShader(): String {
        return  "attribute vec4 aPosition;" + //顶点坐标
                "attribute vec2 aCoordinate;" +//纹理坐标
                //用于传递纹理坐标给片元着色器，命名和片元着色器中的一致
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "  gl_Position = vec4(aPosition.x, -aPosition.y, aPosition.z, 1);" +
                "  vCoordinate = aCoordinate;" +
                "}"
    }

    override fun getFragmentShader(): String {
        //配置float精度，使用了float数据一定要配置：lowp(低)/mediump(中)/highp(高)
        return "precision mediump float;" +
                //从Java传递进入来的纹理单元
                "uniform sampler2D uTexture;" +
                //从顶点着色器传递进来的纹理坐标
                "varying vec2 vCoordinate;" +
                "void main() {" +
                //根据纹理坐标，从纹理单元中取色
                "  vec4 color = texture2D(uTexture, vCoordinate);" +
                "  gl_FragColor = color;" +
                "}"
    }

    override fun onDraw(textureId: Int) {

        GLES20.glUseProgram(mProgram)
        //激活指定纹理单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        //绑定纹理ID到纹理单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        //将激活的纹理单元传递到着色器里面
        GLES20.glUniform1i(mTextureHandle, 0)

        //启用顶点的句柄
        GLES20.glEnableVertexAttribArray(mVertexPosHandle)
        GLES20.glEnableVertexAttribArray(mTexturePosHandle)

        //设置着色器参数， 第二个参数表示一个顶点包含的数据数量，这里为xy，所以为2
        GLES20.glVertexAttribPointer(mVertexPosHandle, 2, GLES20.GL_FLOAT, false, 0, mVertexBuffer)
        GLES20.glVertexAttribPointer(mTexturePosHandle, 2, GLES20.GL_FLOAT, false, 0, mTextureBuffer)

        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)
    }
}