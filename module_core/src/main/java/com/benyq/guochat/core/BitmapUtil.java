package com.benyq.guochat.core;

import android.graphics.Bitmap;

/**
 * @author benyq
 * @time 2021/4/18
 * @e-mail 1520063035@qq.com
 * @note 目前为了方便处理， bitmap都是 ARGB8888格式
 */
public class BitmapUtil {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void gray(Bitmap bitmap);

    public static native void brightness(Bitmap bitmap, float progress);

    public static native void againstWorld(Bitmap bitmap);

    public static native void anaglyph(Bitmap bitmap);
}
