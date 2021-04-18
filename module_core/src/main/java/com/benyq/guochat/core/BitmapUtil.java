package com.benyq.guochat.core;

import android.graphics.Bitmap;

/**
 * @author benyq
 * @time 2021/4/18
 * @e-mail 1520063035@qq.com
 * @note
 */
public class BitmapUtil {
    static {
        System.loadLibrary("native-lib");
    }

    public static native void gray(Bitmap bitmap);

    public static native void brightness(Bitmap bitmap, float progress);
}
