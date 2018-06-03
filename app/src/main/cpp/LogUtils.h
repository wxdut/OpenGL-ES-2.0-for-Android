//
// Created by 王肖 on 2018/6/2.
//

#ifndef OPENGL_ES_2_FOR_ANDROID_LOGUTILS_H
#define OPENGL_ES_2_FOR_ANDROID_LOGUTILS_H

#include <android/log.h>
#include <string>

#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, "jni log (>_<)", format, ##__VA_ARGS__)
#define LOGD(format, ...)  __android_log_print(ANDROID_LOG_DEBUG,  "jni log (^_^)", format, ##__VA_ARGS__)

class LogUtils {
public:
    static void e(std::string s) {
        LOGE("%s", s.c_str());
    }
    static void d(std::string s) {
        LOGD("%s", s.c_str());
    }
};


#endif //OPENGL_ES_2_FOR_ANDROID_LOGUTILS_H
