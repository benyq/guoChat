//
// Created by benyq on 2021/4/18.
//

#ifndef GUOCHAT_BITMAP_UTIL_H
#define GUOCHAT_BITMAP_UTIL_H

#include <jni.h>

void process_bitmap(JNIEnv *env, jobject bitmap, int type, float progress = 1.0);

#endif //GUOCHAT_BITMAP_UTIL_H
