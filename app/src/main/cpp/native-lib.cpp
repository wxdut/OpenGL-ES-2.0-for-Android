#include <jni.h>
#include <string>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>
#include "Model.h"
#include "LogUtils.h"

using namespace std;

Model *model;

jobject assetManager;
JNIEnv *env;
Shader *shader;
glm::mat4 projectionMatrix;

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceCreated(JNIEnv *env, jobject instance, jobject config) {

}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnDrawFrame(JNIEnv *env, jobject instance) {
//    model->onDrawFrame();
}

extern "C"
JNIEXPORT jboolean JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeInit(JNIEnv *jniEnv, jobject instance, jobject am) {
    env = jniEnv;
    assetManager = am;
    model = new Model(env, assetManager);
    LogUtils::d("nativeInit, success~");
    return JNI_TRUE;
}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeOnSurfaceChanged(JNIEnv *env, jclass type,
                                                                         jint width, jint height) {

    // TODO

}