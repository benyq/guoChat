#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include "log_util.h"
#include "bitmap_util.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_gray(JNIEnv *env, jclass clazz, jobject bitmap) {
    process_bitmap(env, bitmap, 1);

    int red = (0xFF547812 & 0x00FF0000) >> 16;
    red = (int)(red + 250) & 0xFF;//84+250=334-78=
    LOGE("red %d", red);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_benyq_guochat_core_BitmapUtil_brightness(JNIEnv *env, jclass clazz, jobject bitmap,
                                                  jfloat progress) {
    process_bitmap(env, bitmap, 2, progress);
}