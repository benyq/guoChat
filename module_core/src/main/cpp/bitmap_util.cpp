//
// Created by benyq on 2021/4/18.
//

#include <android/bitmap.h>
#include "bitmap_util.h"
#include "jni.h"
#include "log_util.h"
#include <malloc.h>
#include <ctime>


void process_bitmap_color(JNIEnv *env, jobject bitmap, int type, float progress) {
    clock_t start = clock();

    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    int* data = NULL;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &data);

    int alpha = 0xFF000000;
    int w = bitmapInfo.width;
    int h = bitmapInfo.height;
    LOGE("pixels %d", w * h);
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

void process_bitmap_shape(JNIEnv *env, jobject bitmap, int type) {
    clock_t start = clock();

    AndroidBitmapInfo bitmapInfo;
    AndroidBitmap_getInfo(env, bitmap, &bitmapInfo);
    int* data = NULL;
    AndroidBitmap_lockPixels(env, bitmap, (void **) &data);

    int w = bitmapInfo.width;
    int h = bitmapInfo.height;

    switch (type) {
        case 1:
            against_world(data, w, h);
            break;
        case 2:
            anaglyph(data, w, h);
            break;
    }

    AndroidBitmap_unlockPixels(env, bitmap);

    clock_t end = clock();
    LOGE("process time %ld ms", (end - start) / 1000);
}

void against_world(int* &data, int width, int height) {

    int middle = height >> 1;

    //下半部分
    for (int i = 0; i < middle; ++i) {
        for (int j = 0; j < width; ++j) {
            data[width * (i + middle) + j] = data[width * i + j];
        }
    }
    //上半部分
    for (int i = 0; i < middle; ++i) {
        for (int j = 0; j < width; ++j) {
            data[width * i + j] = data[width * (height - i) + j];
        }
    }
}

void anaglyph(int* &data, int width, int height) {

    int alpha = 0xFF000000;

    for (int rows = 0; rows < height - 1; rows++)
    {
        for (int cols = 0; cols < width - 1; cols++)
        {

            int pixelsP = data[width * rows + cols];
            int pixelsN = data[width * (rows + 1) + cols + 1];

            int redP = (pixelsP & 0x00FF0000) >> 16;
            int greenP = (pixelsP & 0x0000FF00) >> 8;
            int blueP = pixelsP & 0x000000FF;

            int redN = (pixelsN & 0x00FF0000) >> 16;
            int greenN = (pixelsN & 0x0000FF00) >> 8;
            int blueN = pixelsN & 0x000000FF;

            int red = redP - redN + 128;
            int green = greenP - greenN + 128;
            int blue = blueP - blueN + 128;

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
            data[width * rows + cols] = alpha | (red << 16) | (green << 8) | blue;
        }
    }
}