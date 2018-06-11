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
JNIEXPORT jboolean JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeInit(JNIEnv *jniEnv, jclass type,
                                                             jobject am) {
    env = jniEnv;
    assetManager = am;
    model = new Model(env, assetManager);
//    LogUtils::d("nativeInit, success~");
    return JNI_TRUE;
}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeSurfaceChanged(JNIEnv *env, jclass type,
                                                                       jint width, jint height) {
    model->onSurfaceChanged(width, height);
}

extern "C"
JNIEXPORT void JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_nativeDrawFrame(JNIEnv *env, jclass type) {
    model->onDrawFrame();
}