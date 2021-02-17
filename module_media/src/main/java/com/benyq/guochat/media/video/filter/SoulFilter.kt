package com.benyq.guochat.media.video.filter

import android.opengl.GLES20
import com.benyq.module_base.ext.loge

/**
 * @author benyqYe
 * date 2021/1/20
 * e-mail 1520063035@qq.com
 * description 灵魂出窍滤镜
 * https://www.codercto.com/a/91346.html
 */

class SoulFilter : BaseFilter(){

    private var mTimeHandle = -1
    private var mRunTime = 0f

    override fun getLocations() {
        mVertexPosHandle = GLES20.glGetAttribLocation(mProgram, "aPosition")
        mTexturePosHandle = GLES20.glGetAttribLocation(mProgram, "aCoordinate")

        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "uTexture")

        mTimeHandle = GLES20.glGetUniformLocation(mProgram, "time")
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
                "uniform float time;"+
                "void main() {" +
                // 一次灵魂出窍效果的时长 0.7
                "float duration = 0.7;"+
                // 透明度上限
                "float maxAlpha = 0.55;"+
                // 放大图片上限
                "float maxScale = 1.8;"+
                // 进度值[0,1]
//                "float progress = mod(time, duration) / duration;"+
                "float progress = mod(time, duration) / duration;"+
                // 透明度[0,0.4]
                "float alpha = maxAlpha * (1.0 - progress);"+
                // 缩放比例[1.0,1.8]
                "float scale = 1.0 + (maxScale - 1.0) * progress;"+
                // 放大纹理坐标
                // 根据放大比例，得到放大纹理坐标 [0,0],[0,1],[1,1],[1,0]
                "float weakX = 0.5 + (vCoordinate.x - 0.5) / scale; \n" +
                "float weakY = 0.5 + (vCoordinate.y - 0.5) / scale; \n" +
                // 放大纹理坐标
                "vec2 weakTextureCoords = vec2(weakX, weakY); \n" +
                // 获取对应放大纹理坐标下的纹素(颜色值rgba)
                "vec4 weakMask = texture2D(uTexture, weakTextureCoords);\n" +
                // 原始的纹理坐标下的纹素(颜色值rgba)
                "vec4 mask = texture2D(uTexture, vCoordinate); \n" +
                // 颜色混合 默认颜色混合方程式 = mask * (1.0-alpha) + weakMask * alpha;
                "gl_FragColor = mask * (1.0 - alpha) + weakMask * alpha;\n"+
                "}"
    }
}