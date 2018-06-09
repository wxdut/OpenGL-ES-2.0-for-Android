//
// Created by hiwang on 2018/5/30.
//

#ifndef OPENGL_ES_2_0_FOR_ANDROID_MODEL_H
#define OPENGL_ES_2_0_FOR_ANDROID_MODEL_H

#include <assimp/scene.h>
#include <glm/ext.hpp>
#include "Mesh.h"
#include "Shader.h"
#include "jni.h"

extern glm::mat4 projectionMatrix;

class Model {

public:
    /*  Model Data */
    vector<Texture> textures_loaded;    // stores all the textures loaded so far, optimization to make sure textures aren't loaded more than once.
    vector<Mesh> meshes;
    string directory;

    Model(JNIEnv *env, jobject assetManager);

private:
    // loads a model with supported ASSIMP extensions from file and stores the resulting meshes in the meshes vector.
    void loadModel(JNIEnv *env);

    // processes a node in a recursive fashion. Processes each individual mesh located at the node and repeats this process on its children nodes (if any).
    void processNode(aiNode *node, const aiScene *scene);

    Mesh processMesh(aiMesh *mesh, const aiScene *scene);

    vector<Texture> loadMaterialTextures(aiMaterial *mat, aiTextureType type, string typeName);

    void draw();


public:
    void onSurfaceChanged(jint width, jint height) {
        projectionMatrix = glm::perspective(90.0, width / (height * 1.0), 1.0, 100.0);
    }
    void onDrawFrame() {
        draw();
    }
};


#endif //OPENGL_ES_2_0_FOR_ANDROID_MODEL_H
