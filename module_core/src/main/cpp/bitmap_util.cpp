//
// Created by benyq on 2021/4/18.
//

#include <android/bitmap.h>
#include "bitmap_util.h"
#include "jni.h"
#include "log_util.h"
#include <ctime>


void process_bitmap(JNIEnv *env, jobject bitmap, int type, float progress) {
    clock_t start = clock();

    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    int* data = NULL;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &data);

    int alpha = 0xFF000000;
    int w = bitmapInfo.width;
    int h = bitmapInfo.height;

    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            int color = data[w * i + j];
            int red = (color & 0x00FF0000) >> 16;
            int green = (color & 0x0000FF00) >> 8;
            int blue = color & 0x000000FF;

            switch (type) {
                case 1:
                    color = (red + green + blue) / 3;
                    color = alpha | (color << 16) | (color << 8) | color;
                    data[w * i + j] = color;
                    break;
                case 2:
                    red = (int)(red + progress);
                    green = (int)(green + progress);
                    blue = (int)(blue + progress);

                    if (red > 0xFF) {
                        red = 0xFF;
                    }
                    if (red < 0) {
                        red = 0;
                    }
                    if (green > 0xFF) {
                        green = 0xFF;
                    }
                    if (green < 0) {
                        green = 0;
                    }
                    if (blue > 0xFF) {
                        blue = 0xFF;
                    }
                    if (blue < 0) {
                        blue = 0;
                    }
                    color = alpha | (red << 16) | (green << 8) | blue;
                    data[w * i + j] = color;
                    break;
            }


        }
    }


    AndroidBitmap_unlockPixels(env, bitmap);

    clock_t end = clock();
    LOGE("process time %ld ms", (end - start) / 1000);
}

