//
// Created by hiwang on 2018/5/25.
//

#ifndef OPENGL_ES_2_0_FOR_ANDROID_MESH_H
#define OPENGL_ES_2_0_FOR_ANDROID_MESH_H

#include <string>
#include <glm/glm.hpp>
#include <vector>

using namespace std;

struct Vertex {
    glm::vec3 position;
    glm::vec3 normal;
    glm::vec2 texCoords;
};
struct Texture {
    unsigned int id;
    string type;
    string path;
};

class Mesh {
public:
    /*  Mesh Data  */
    vector<Vertex> vertices;
    vector<unsigned int> indices;
    vector<Texture> textures;
    unsigned int vbo, ibo;

    /*  Functions  */
    Mesh(vector<Vertex> vertices, vector<unsigned int> indices, vector<Texture> textures);

    void draw();

private:
    void setupMesh();
};


#endif //OPENGL_ES_2_0_FOR_ANDROID_MESH_H
