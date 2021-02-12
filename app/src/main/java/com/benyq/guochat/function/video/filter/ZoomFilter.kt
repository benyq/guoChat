package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.module_base.ext.loge

/**
 * @author benyqYe
 * date 2021/1/21
 * e-mail 1520063035@qq.com
 * description 缩放滤镜
 * https://juejin.cn/post/6844904035066069000
 */

class ZoomFilter : BaseFilter(){

    private var mTimeHandle = -1
    private var mRunTime = 0f


    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mTimeHandle = GLES20.glGetUniformLocation(mProgram, "Time")
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
        GLES20.glUniform1f(mTimeHandle, mRunTime)
        //开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        GLES20.glUseProgram(0)
        mRunTime += 0.02f
    }

    override fun getFragmentShader(): String {
        //配置float精度，使用了float数据一定要配置：lowp(低)/mediump(中)/highp(高)
        return "precision mediump float;" +
                //从Java传递进入来的纹理单元
                "uniform sampler2D uTexture;" +
                //从顶点着色器传递进来的纹理坐标
                "varying vec2 vCoordinate;" +
                "uniform float Time;"+
                "const float PI = 3.1415926;"+
                "void main (void) {" +
                // 缩放时间周期
                "    float duration = 0.6;" +
                //缩放增加的最大值
                "    float maxAmplitude = 0.3;" +
                //缩放中心点
                "    vec2 anchorPoint = vec2(0.5, 0.5);" +
                //根据当前时间进度计算缩放幅度
                "    float time = mod(Time, duration);" +
                "    float amplitude = 1.0 + maxAmplitude * abs(sin(time * (PI / duration)));" +
                //转化纹理坐标变化
                "    vec2 textCoords = vCoordinate;" +
                "    textCoords = vec2(anchorPoint.x + (textCoords.x - anchorPoint.x) / amplitude, anchorPoint.y + (textCoords.y - anchorPoint.y) / amplitude);" +
                "    vec4 mask = texture2D(uTexture, textCoords);" +
                "    gl_FragColor = vec4(mask.rgb, 1.0);" +
                "}"
    }
}