//
// Created by hiwang on 2018/5/30.
//

#include <jni.h>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>
#include <assimp/Importer.hpp>
#include <assimp/postprocess.h>
#include <ostream>
#include "Model.h"
#include "LogUtils.h"
#include "TextureHelper.h"
#include <vector>
#include <GLES2/gl2.h>
#define STB_IMAGE_IMPLEMENTATION
#include "stb_image.h"

using namespace std;


extern jobject assetManager;
extern JNIEnv *env;
extern Shader *shader;
extern glm::mat4 projectionMatrix;

unsigned int TextureFromFile(const char *path, const string &directory);

Model::Model(JNIEnv *jniEnv, jobject am) {
    env = jniEnv;
    assetManager = am;
    shader = new Shader();
    loadModel(env);
}

void Model::loadModel(JNIEnv *env) {

    // 从得到的buffer生成scene
    Assimp::Importer *importer = new Assimp::Importer();
    const aiScene *scene = importer->ReadFile("/storage/emulated/0/AA_S9/opengl_3d_models/nanosuit/nanosuit.obj", aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_ValidateDataStructure | aiProcess_JoinIdenticalVertices | aiProcess_SplitLargeMeshes);
    LogUtils::e("loadModel, importError = " + string(importer->GetErrorString()));
    if (scene == NULL) {
        string msg;
        msg.append("scene is null, ").append(importer->GetErrorString());
        LogUtils::e(msg);
        return;
    }
    if (scene->mNumMeshes <= 0) {
        LogUtils::e("scene->mNumMeshes is 0");
        return;
    }
    processNode(scene->mRootNode, scene);
}

void Model::processNode(aiNode *node, const aiScene *scene) {
    // process each mesh located at the current node
    for (int i = 0; i < node->mNumMeshes; i++) {
        aiMesh *mesh = scene->mMeshes[node->mMeshes[i]];
        meshes.push_back(processMesh(mesh, scene));
    }
    // after we've processed all of the meshes (if any) we then recursively process each of the children nodes
    for (unsigned int i = 0; i < node->mNumChildren; i++) {
        processNode(node->mChildren[i], scene);
    }
}

Mesh Model::processMesh(aiMesh *mesh, const aiScene *scene) {
    // data to fill
    vector<Vertex> vertices;
    vector<unsigned int> indices;
    vector<Texture> textures;
    // Walk through each of the mesh's vertices
    for (int i = 0; i < mesh->mNumVertices; i++) {
        Vertex vertex;
        vertex.position.x = mesh->mVertices[i].x;
        vertex.position.y = mesh->mVertices[i].y;
        vertex.position.z = mesh->mVertices[i].z;
        if (mesh->mNormals != NULL) { // 不一定会有normals
//            vertex.normal.x = mesh->mNormals[i].x;
//            vertex.normal.y = mesh->mNormals[i].y;
//            vertex.normal.z = mesh->mNormals[i].z;
        }
        // texture coordinates
        if (mesh->mTextureCoords[0]) // does the mesh contain texture coordinates?
        {
            // a vertex can contain up to 8 different texture coordinates. We thus make the assumption that we won't
            // use models where a vertex can have multiple texture coordinates so we always take the first set (0).
            vertex.texCoords.x = mesh->mTextureCoords[0][i].x;
            vertex.texCoords.y = mesh->mTextureCoords[0][i].y;
        } else {
            vertex.texCoords = glm::vec2(0.0f, 0.0f);
        }
        vertices.push_back(vertex);
    }
    // now wak through each of the mesh's faces (a face is a mesh its triangle) and retrieve the corresponding vertex indices.
    for (unsigned int i = 0; i < mesh->mNumFaces; i++) {
        aiFace face = mesh->mFaces[i];
        // retrieve all indices of the face and store them in the indices vector
        assert(face.mNumIndices == 3);
        for (unsigned int j = 0; j < face.mNumIndices; j++) {
            indices.push_back(face.mIndices[j]);
        }
    }
    // process materials
    aiMaterial *material = scene->mMaterials[mesh->mMaterialIndex];
    // we assume a convention for sampler names in the shaders. Each diffuse texture should be named
    // as 'texture_diffuseN' where N is a sequential number ranging from 1 to MAX_SAMPLER_NUMBER.
    // Same applies to other texture as the following list summarizes:
    // diffuse: texture_diffuseN
    // specular: texture_specularN
    // normal: texture_normalN

    // 1. diffuse maps
    vector<Texture> diffuseMaps = loadMaterialTextures(material, aiTextureType_DIFFUSE,
                                                       "texture_diffuse");
    textures.insert(textures.end(), diffuseMaps.begin(), diffuseMaps.end());
    // 2. specular maps
    vector<Texture> specularMaps = loadMaterialTextures(material, aiTextureType_SPECULAR,
                                                        "texture_specular");
    textures.insert(textures.end(), specularMaps.begin(), specularMaps.end());

    // return a mesh object created from the extracted mesh data
    return Mesh(vertices, indices, textures);
}

// checks all material textures of a given type and loads the textures if they're not loaded yet.
// the required info is returned as a Texture struct.
vector<Texture> Model::loadMaterialTextures(aiMaterial *mat, aiTextureType type, string typeName) {
    vector<Texture> textures;
    for (unsigned int i = 0; i < mat->GetTextureCount(type); i++) {
        aiString str;
        mat->GetTexture(type, i, &str);
        // check if texture was loaded before and if so, continue to next iteration: skip loading a new texture
        bool skip = false;
        for (unsigned int j = 0; j < textures_loaded.size(); j++) {
            if (std::strcmp(textures_loaded[j].path.data(), str.C_Str()) == 0) {
                textures.push_back(textures_loaded[j]);
                skip = true; // a texture with the same filepath has already been loaded, continue to next one. (optimization)
                break;
            }
        }
        if (!skip) {   // if texture hasn't been loaded already, load it
            Texture texture;
            texture.id = TextureFromFile(str.C_Str(), "/storage/emulated/0/AA_S9/opengl_3d_models/nanosuit");
            texture.type = typeName;
            texture.path = str.C_Str();
            textures.push_back(texture);
            textures_loaded.push_back(
                    texture);  // store it as texture loaded for entire model, to ensure we won't unnecesery load duplicate textures.
        }
    }
    return textures;
}

void Model::draw() {

    glClearColor(1, 1, 1, 1);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glEnable(GL_DEPTH_TEST);

    glUniformMatrix4fv(shader->uMatrixHandle, 1, GL_FALSE,
                       glm::value_ptr(projectionMatrix));

    for(unsigned int i = 0; i < meshes.size(); i++) {
        meshes[i].draw();
    }
}

unsigned int TextureFromFile(const char *path, const string &directory) {
    string filename = string(path);
    filename = directory + '/' + filename;

    unsigned int textureID = TextureHelper::obtain();

    int width, height, nrComponents;
    unsigned char *data = stbi_load(filename.c_str(), &width, &height, &nrComponents, 0);
    if (data) {
        GLenum format;
        if (nrComponents == 3)
            format = GL_RGB;
        else if (nrComponents == 4)
            format = GL_RGBA;
        glActiveTexture(GL_TEXTURE0 + textureID);
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        stbi_image_free(data);
    } else {
        string msg;
        msg.append("Texture failed to load at path: ").append(path);
        LogUtils::e(msg);
        stbi_image_free(data);
    }

    return textureID;
}

