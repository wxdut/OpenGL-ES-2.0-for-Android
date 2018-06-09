#include <jni.h>
#include <string>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>
#include "Model.h"
#include "LogUtils.h"

using namespace std;

Model* model;

//extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceCreated(JNIEnv *env,
                                                                         jobject instance,
                                                                         jobject config) {
}

//extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceChanged(JNIEnv *env,
                                                                         jobject instance,
                                                                         jint width, jint height) {
    model->onSurfaceChanged(width, height);
}

//extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnDrawFrame(JNIEnv *env, jobject instance) {
    model->onDrawFrame();
}

//extern "C"
JNIEXPORT jboolean JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeInit(JNIEnv *env, jobject instance, jobject assetManager) {
    model = new Model(env, assetManager);
    LogUtils::d("nativeInit, success~");

    return JNI_TRUE;
}