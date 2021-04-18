//
// Created by benyq on 2021/4/18.
//

#ifndef GUOCHAT_LOG_UTIL_H
#define GUOCHAT_LOG_UTIL_H

#include <android/log.h>

#define TAG "chat_ndk_log"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型


#endif //GUOCHAT_LOG_UTIL_H
