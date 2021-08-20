//
// Created by benyq on 2021/4/18.
//

#ifndef GUOCHAT_BITMAP_UTIL_H
#define GUOCHAT_BITMAP_UTIL_H

#include <jni.h>

void process_bitmap_color(JNIEnv *env, jobject bitmap, int type, float progress = 1.0);

void process_bitmap_shape(JNIEnv *env, jobject bitmap, int type);

void against_world(int* &data, int width, int height);

void anaglyph(int* &data, int width, int height);

#endif //GUOCHAT_BITMAP_UTIL_H
