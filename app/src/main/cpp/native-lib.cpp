#include <jni.h>
#include <string>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>
#include "Model.h"
#include "LogUtils.h"

using namespace std;

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceCreated(JNIEnv *env,
                                                                         jobject instance,
                                                                         jobject config) {
    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceChanged(JNIEnv *env,
                                                                         jobject instance,
                                                                         jint width, jint height) {
    // TODO

}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnDrawFrame(JNIEnv *env, jobject instance) {
    // TODO

}

extern "C"
JNIEXPORT jboolean JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeInit(JNIEnv *env, jobject instance,
                                                             jobject assetManager) {
    Model model(env, assetManager);
    LogUtils::d("nativeInit, success~");

    return JNI_TRUE;
}