#include <jni.h>
#include <string>
#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>

extern "C" JNIEXPORT jstring

JNICALL
Java_wxplus_opengles2forandroid_OpenGL_106_1Model_stringFromJNI(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from C++";
    Assimp::Importer importer;
    const aiScene *scene = importer.ReadFile("", aiProcess_Triangulate | aiProcess_FlipUVs);
    return env->NewStringUTF(hello.c_str());
}