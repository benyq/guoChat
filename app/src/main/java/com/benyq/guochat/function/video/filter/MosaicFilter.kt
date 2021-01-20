package com.benyq.guochat.function.video.filter

import android.opengl.GLES20
import com.benyq.guochat.function.video.FrameBuffer
import com.benyq.guochat.function.video.OpenGLTools
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * @author benyq
 * @time 2021/1/16
 * @e-mail 1520063035@qq.com
 * @note 三角形马赛克滤镜
 * shader来源
 * https://juejin.cn/post/6844903889569841160
 */
class MosaicFilter : BaseFilter(){

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
                "float mosaicSize = 0.03;"+
                //从顶点着色器传递进来的纹理坐标
                "varying vec2 vCoordinate;" +
                "void main() {" +
                "    const float TR = 0.866025;\n" +
                "    const float PI6 = 0.523599;\n" +
                "    float x = vCoordinate.x;\n" +
                "    float y = vCoordinate.y;\n" +
                "    int wx = int(x/(1.5 * mosaicSize));\n" +
                "    int wy = int(y/(TR * mosaicSize));\n" +
                "    vec2 v1, v2, vn;\n" +
                "    if (wx / 2 * 2 == wx) {\n" +
                "        if (wy/2 * 2 == wy) {\n" +
                "            v1 = vec2(mosaicSize * 1.5 * float(wx), mosaicSize * TR * float(wy));\n" +
                "            v2 = vec2(mosaicSize * 1.5 * float(wx + 1), mosaicSize * TR * float(wy + 1));\n" +
                "        } else {\n" +
                "            v1 = vec2(mosaicSize * 1.5 * float(wx), mosaicSize * TR * float(wy + 1));\n" +
                "            v2 = vec2(mosaicSize * 1.5 * float(wx + 1), mosaicSize * TR * float(wy));\n" +
                "        }\n" +
                "    } else {\n" +
                "        if (wy/2 * 2 == wy) {\n" +
                "            v1 = vec2(mosaicSize * 1.5 * float(wx), mosaicSize * TR * float(wy + 1));\n" +
                "            v2 = vec2(mosaicSize * 1.5 * float(wx+1), mosaicSize * TR * float(wy));\n" +
                "        } else {\n" +
                "            v1 = vec2(mosaicSize * 1.5 * float(wx), mosaicSize * TR * float(wy));\n" +
                "            v2 = vec2(mosaicSize * 1.5 * float(wx + 1), mosaicSize * TR * float(wy+1));\n" +
                "        }\n" +
                "    }\n" +
                "    float s1 = sqrt(pow(v1.x - x, 2.0) + pow(v1.y - y, 2.0));\n" +
                "    float s2 = sqrt(pow(v2.x - x, 2.0) + pow(v2.y - y, 2.0));\n" +
                "    if (s1 < s2) {\n" +
                "        vn = v1;\n" +
                "    } else {\n" +
                "        vn = v2;\n" +
                "    }\n" +
                "    \n" +
                "    vec4 mid = texture2D(uTexture, vn);\n" +
                "    float a = atan((x - vn.x)/(y - vn.y));\n" +
                "    vec2 area1 = vec2(vn.x, vn.y - mosaicSize * TR / 2.0);\n" +
                "    vec2 area2 = vec2(vn.x + mosaicSize / 2.0, vn.y - mosaicSize * TR / 2.0);\n" +
                "    vec2 area3 = vec2(vn.x + mosaicSize / 2.0, vn.y + mosaicSize * TR / 2.0);\n" +
                "    vec2 area4 = vec2(vn.x, vn.y + mosaicSize * TR / 2.0);\n" +
                "    vec2 area5 = vec2(vn.x - mosaicSize / 2.0, vn.y + mosaicSize * TR / 2.0);\n" +
                "    vec2 area6 = vec2(vn.x - mosaicSize / 2.0, vn.y - mosaicSize * TR / 2.0);\n" +
                "    \n" +
                "    if (a >= PI6 && a < PI6 * 3.0) {\n" +
                "        vn = area1;\n" +
                "    } else if (a >= PI6 * 3.0 && a < PI6 * 5.0) {\n" +
                "        vn = area2;\n" +
                "    } else if ((a >= PI6 * 5.0 && a <= PI6 * 6.0) || (a < -PI6 * 5.0 && a > -PI6 * 6.0)) {\n" +
                "        vn = area3;\n" +
                "    } else if (a < -PI6 * 3.0 && a >= -PI6 * 5.0) {\n" +
                "        vn = area4;\n" +
                "    } else if(a <= -PI6 && a> -PI6 * 3.0) {\n" +
                "        vn = area5;\n" +
                "    } else if (a > -PI6 && a < PI6) {\n" +
                "        vn = area6;\n" +
                "    }\n" +
                "    vec4 color = texture2D(uTexture, vn);\n" +
                "    gl_FragColor = color;\n" +
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

        mFrameBuffer?.unbind()
    }

}