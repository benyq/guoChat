package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.module_base.ext.loge

/**
 * @author benyqYe
 * date 2021/1/21
 * e-mail 1520063035@qq.com
 * description 缩放滤镜
 * https://www.codercto.com/a/91346.html
 */

class ZoomFilter2 : BaseFilter(){

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
                "void main (){ " +
                // 一次抖动滤镜的时长 0.7
                "float duration = 0.7;" +
                // 放大图片上限
                "float maxScale = 1.1;" +
                // 颜色偏移步长
                "float offset = 0.02;" +
                // 进度[0,1]
                "float progress = mod(Time, duration) / duration;" +
                // 颜色偏移值范围[0,0.02]
                "vec2 offsetCoords = vec2(offset, offset) * progress; " +
                // 缩放范围[1.0-1.1];
                "float scale = 1.0 + (maxScale - 1.0) * progress; " +
                // 放大纹理坐标.
                "vec2 ScaleTextureCoords = vec2(0.5, 0.5) + (vCoordinate - vec2(0.5, 0.5)) / scale;" +
                // 获取3组颜色rgb
                // 原始颜色+offsetCoords
                "vec4 maskR = texture2D(uTexture, ScaleTextureCoords + offsetCoords);" +
                // 原始颜色-offsetCoords
                "vec4 maskB = texture2D(uTexture, ScaleTextureCoords - offsetCoords);" +
                // 原始颜色
                "vec4 mask = texture2D(uTexture, ScaleTextureCoords);" +
                // 从3组来获取颜色:
                // maskR.r,mask.g,maskB.b 注意这3种颜色取值可以打乱或者随意发挥.不一定写死.只是效果会有不一样.大家可以试试.
                // mask.a 获取原图的透明度
                "gl_FragColor = vec4(maskR.r, mask.g, maskB.b, mask.a);}"
    }
}