package com.benyq.guochat.function.video.filter

import android.opengl.GLES20

/**
 * @author benyqYe
 * date 2021/1/22
 * e-mail 1520063035@qq.com
 * description GPUImage项目里面搞来的
 */

class CartoonFilter : BaseFilter(){

    private var mThresholdLocationHandle = -1
    private var mQuantizationLevelsLocation = -1

    private var mUniformTexelWidthLocation = -1
    private var mUniformTexelHeightLocation = -1

    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "position")
        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "inputTextureCoordinate")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "inputImageTexture")

        mThresholdLocationHandle = GLES20.glGetUniformLocation(mProgram, "threshold")
        mQuantizationLevelsLocation = GLES20.glGetUniformLocation(mProgram, "quantizationLevels")

        mUniformTexelWidthLocation = GLES20.glGetUniformLocation(mProgram, "texelWidth")
        mUniformTexelHeightLocation = GLES20.glGetUniformLocation(mProgram, "texelHeight")

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
        GLES20.glVertexAttribPointer(
            mTexturePosHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            0,
            mTextureBuffer
        )
        GLES20.glUniform1f(mThresholdLocationHandle, 0.2f)
        GLES20.glUniform1f(mQuantizationLevelsLocation, 10.0f)

        GLES20.glUniform1f(mUniformTexelWidthLocation, 4.5f / mWidth)
        GLES20.glUniform1f(mUniformTexelHeightLocation, 4.5f / mHeight)
        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)
    }

    override fun getVertexShader(): String {
        return "attribute vec4 position;" +
                "attribute vec4 inputTextureCoordinate;" +
                "" +
                "uniform highp float texelWidth; " +
                "uniform highp float texelHeight; " +
                "" +
                "varying vec2 textureCoordinate;" +
                "varying vec2 leftTextureCoordinate;" +
                "varying vec2 rightTextureCoordinate;" +
                "" +
                "varying vec2 topTextureCoordinate;" +
                "varying vec2 topLeftTextureCoordinate;" +
                "varying vec2 topRightTextureCoordinate;" +
                "" +
                "varying vec2 bottomTextureCoordinate;" +
                "varying vec2 bottomLeftTextureCoordinate;" +
                "varying vec2 bottomRightTextureCoordinate;" +
                "" +
                "void main()" +
                "{" +
                "    gl_Position = vec4(position.x, -position.y, position.z, 1);;" +
                "" +
                "    vec2 widthStep = vec2(texelWidth, 0.0);" +
                "    vec2 heightStep = vec2(0.0, texelHeight);" +
                "    vec2 widthHeightStep = vec2(texelWidth, texelHeight);" +
                "    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);" +
                "" +
                "    textureCoordinate = inputTextureCoordinate.xy;" +
                "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;" +
                "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;" +
                "" +
                "    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;" +
                "    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;" +
                "    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;" +
                "" +
                "    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;" +
                "    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;" +
                "    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;" +
                "}"
    }

    override fun getFragmentShader(): String {
        return "precision highp float;\n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 leftTextureCoordinate;\n" +
                "varying vec2 rightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 topTextureCoordinate;\n" +
                "varying vec2 topLeftTextureCoordinate;\n" +
                "varying vec2 topRightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 bottomTextureCoordinate;\n" +
                "varying vec2 bottomLeftTextureCoordinate;\n" +
                "varying vec2 bottomRightTextureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "uniform highp float intensity;\n" +
                "uniform highp float threshold;\n" +
                "uniform highp float quantizationLevels;\n" +
                "\n" +
                "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "\n" +
                "float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
                "float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
                "float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
                "float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
                "float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
                "float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
                "float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
                "float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
                "float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
                "float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
                "\n" +
                "float mag = length(vec2(h, v));\n" +
                "\n" +
                "vec3 posterizedImageColor = floor((textureColor.rgb * quantizationLevels) + 0.5) / quantizationLevels;\n" +
                "\n" +
                "float thresholdTest = 1.0 - step(threshold, mag);\n" +
                "\n" +
                "gl_FragColor = vec4(posterizedImageColor * thresholdTest, textureColor.a);\n" +
                "}\n"
    }
}