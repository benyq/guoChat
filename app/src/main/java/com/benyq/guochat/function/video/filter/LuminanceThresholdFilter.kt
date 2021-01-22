package com.benyq.guochat.function.video.filter

import android.opengl.GLES20

/**
 * @author benyqYe
 * date 2021/1/21
 * e-mail 1520063035@qq.com
 * description 亮度阈值滤镜，来自 GPUImage 项目
 */

class LuminanceThresholdFilter : BaseFilter() {

    private var mThresholdHandle = -1

    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")
        mThresholdHandle = GLES20.glGetUniformLocation(mProgram, "threshold")
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
        GLES20.glUniform1f(mThresholdHandle, 0.6f)
        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)
    }

    override fun getFragmentShader(): String {
        return "precision highp float;" +
                "varying vec2 vCoordinate;" +
                "uniform sampler2D uTexture;" +
                "uniform float threshold;" +
                "" +
                "const vec3 W = vec3(0.2125, 0.7154, 0.0721);" +
                "" +
                "void main()" +
                "{" +
                "    vec4 textureColor = texture2D(uTexture, vCoordinate);" +
                "    float luminance = dot(textureColor.rgb, W);" +
                "    float thresholdResult = step(threshold, luminance);" +
                "    gl_FragColor = vec4(vec3(thresholdResult), textureColor.w);" +
                "}"
    }
}