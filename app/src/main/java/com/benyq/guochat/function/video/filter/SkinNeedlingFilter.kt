package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.mvvm.ext.loge

class SkinNeedlingFilter : BaseFilter(){

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
        mRunTime += 0.05f
    }

    override fun getFragmentShader(): String {
        //配置float精度，使用了float数据一定要配置：lowp(低)/mediump(中)/highp(高)
        return "precision mediump float;" +
                //从Java传递进入来的纹理单元
                "uniform sampler2D uTexture;" +
                //从顶点着色器传递进来的纹理坐标
                "varying vec2 vCoordinate;" +
                "uniform float Time; " +
                "const float PI = 3.1415926;"+
                // 随机数
                "float rand(float n) { " +
                    //fract(x),返回x的小数部分数据
                "   return fract(sin(n) * 43758.5453123); " +
                "}" +
                "void main (void) { " +
                // 最大抖动
                " float maxJitter = 0.06;" +
                // 一次毛刺滤镜的时长
                "float duration = 0.3; " +
                // 红色颜色偏移量
                "float colorROffset = 0.01; " +
                // 绿色颜色偏移量
                "float colorBOffset = -0.025;" +
                // 时间周期[0.0,0.6];
                " float time = mod(Time, duration * 2.0);" +
                // 振幅:[0,1];
                "float amplitude = max(sin(time * (PI / duration)), 0.0);" +
                // 像素随机偏移[-1,1]
                "float jitter = rand(vCoordinate.y) * 2.0 - 1.0; " +
                // 是否要做偏移.
                "bool needOffset = abs(jitter) < maxJitter * amplitude; " +
                // 获取纹理X值.根据needOffset，来计算它X撕裂.
                // needOffset = YES，撕裂较大;
                // needOffset = NO，撕裂较小.
                "float textureX = vCoordinate.x + (needOffset ? jitter : (jitter * amplitude * 0.006)); " +
                // 撕裂后的纹理坐标x,y
                "vec2 textureCoords = vec2(textureX, vCoordinate.y); "+
                // 颜色偏移3组颜色
                // 根据撕裂后获取的纹理颜色值
                "vec4 mask = texture2D(uTexture, textureCoords); " +
                // 撕裂后的纹理颜色偏移
                "vec4 maskR = texture2D(uTexture, textureCoords + vec2(colorROffset * amplitude, 0.0)); " +
                // 撕裂后的纹理颜色偏移
                "vec4 maskB = texture2D(uTexture, textureCoords + vec2(colorBOffset * amplitude, 0.0)); " +
                // 红色/蓝色部分发生撕裂.
                "gl_FragColor = vec4(maskR.r, mask.g, maskB.b, mask.a);" +
                " }"
    }
}