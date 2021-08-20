#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include "log_util.h"
#include "bitmap_util.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_gray(JNIEnv *env, jclass clazz, jobject bitmap) {
    process_bitmap_color(env, bitmap, 1);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_brightness(JNIEnv *env, jclass clazz, jobject bitmap,
                                                  jfloat progress) {
    process_bitmap_color(env, bitmap, 2, progress);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_anaglyph(JNIEnv *env, jclass clazz, jobject bitmap) {
    process_bitmap_shape(env, bitmap, 2);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_againstWorld(JNIEnv *env, jclass clazz, jobject bitmap) {
    process_bitmap_shape(env, bitmap, 1);
}