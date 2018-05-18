#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring

JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_stringFromJNI(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}